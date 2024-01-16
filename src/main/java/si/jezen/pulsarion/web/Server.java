package si.jezen.pulsarion.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import si.jezen.pulsarion.Pulsarion;
import si.jezen.pulsarion.web.models.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

public class Server {
    private final int port;

    private Connection database;
    private final Service http;
    private final FreeMarkerEngine freeMarkerEngine;

    private Authentication auth;

    private File dataLocation;

    int donationGoal;

    Pulsarion plugin;

    public Server(FileConfiguration config, File dataLocation, Connection database, Pulsarion instance) {
        this.port = config.getInt("port", 8080);
        this.http = Service.ignite().port(port);
        this.database = database;
        this.dataLocation = dataLocation;
        this.donationGoal = config.getInt("settings.donation-goal");
        this.plugin = instance;

        this.auth = new Authentication(config.getString("administration.username"), config.getString("administration.password"));

        // Configure FreeMarker
        Configuration freeMarkerConfiguration = new Configuration(Configuration.VERSION_2_3_0);
        try {
            // Set the directory where FreeMarker templates are located
            File templatesDir = dataLocation;
            freeMarkerConfiguration.setDirectoryForTemplateLoading(templatesDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set FreeMarker templates directory", e);
        }

        this.freeMarkerEngine = new FreeMarkerEngine(freeMarkerConfiguration);

        // Set up the static file location
        setupStaticFiles(dataLocation);
    }

    public void stop() {
        http.stop();
    }

    private void authenticate(Request req, Response res) {
        String authToken = req.cookie("AuthToken");
        if (authToken != null) {
            if (!authToken.equals(auth.getAuthToken())) {
                res.redirect("/admin/login");
            }
        } else {
            res.redirect("/admin/login");
        }
    }
    public void serve() {
        // Example route
        http.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("featured", Product.getFeaturedProducts(database));
            model.put("purchases", Purchase.getLastFivePurchases(database));
            model.put("donations", Purchase.getMonthlyTotal(database));
            model.put("donationGoal", donationGoal);
            model.put("donationGoalPercent", Purchase.getMonthlyTotal(database) * 100 / donationGoal);
            return new ModelAndView(model, "views/home.ftl");
        }, freeMarkerEngine);
        
        http.get("/store", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String view = req.queryParams("view");

            model.put("categories", Category.getCategories(database));
            model.put("featured", Product.getFeaturedProducts(database));
            model.put("purchases", Purchase.getLastFivePurchases(database));
            model.put("donations", Purchase.getMonthlyTotal(database));
            model.put("donationGoal", donationGoal);
            model.put("donationGoalPercent", Purchase.getMonthlyTotal(database) * 100 / donationGoal);

            if (view != null) {
                try {
                    int categoryId = Integer.parseInt(view);
                    Category category = Category.getCategoryById(database, categoryId);
                    if (category != null) {
                        model.put("category", category); // Add category to the model
                        List<Product> products = Product.getProductsFromCategory(database, category.getId());
                        model.put("products", products);
                        model.put("view", view);
                    } else {
                        // Handle category not found
                        res.status(404);
                        return new ModelAndView(model, "views/error.ftl");
                    }
                } catch (NumberFormatException e) {
                    res.status(400);
                    return new ModelAndView(model, "views/error.ftl");
                }
            }

            return new ModelAndView(model, "views/store.ftl");
        }, freeMarkerEngine);

        http.get("/tos", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("categories", Category.getCategories(database));
            model.put("featured", Product.getFeaturedProducts(database));
            model.put("purchases", Purchase.getLastFivePurchases(database));
            model.put("donations", Purchase.getMonthlyTotal(database));
            model.put("donationGoal", donationGoal);
            model.put("donationGoalPercent", Purchase.getMonthlyTotal(database) * 100 / donationGoal);
            return new ModelAndView(model, "views/tos.ftl");
        }, freeMarkerEngine);

        http.get("/order", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("categories", Category.getCategories(database));
            model.put("featured", Product.getFeaturedProducts(database));
            model.put("purchases", Purchase.getLastFivePurchases(database));
            model.put("donations", Purchase.getMonthlyTotal(database));
            model.put("donationGoal", donationGoal);
            model.put("donationGoalPercent", Purchase.getMonthlyTotal(database) * 100 / donationGoal);

            model.put("product_id", Integer.parseInt(req.queryParams("id")));
            return new ModelAndView(model, "views/order.ftl");
        }, freeMarkerEngine);

        http.post("/order", (request, response) -> {
            FileConfiguration config = plugin.getConfig();
            // Extract data from the request
            String email = request.queryParams("email");
            String minecraftUsername = request.queryParams("minecraftUsername");
            String productId = request.queryParams("product_id");

            Product product = Product.getProductById(database, Integer.parseInt(productId));
            // Here, you would typically process the data,
            // e.g., validate it, store it in your database, etc.

            String validation_string = RandomStringUtils.randomAlphabetic(8);
            Purchase.addPurchase(database, email, Integer.valueOf(productId), minecraftUsername, product.getPrice(), validation_string, new Timestamp(System.currentTimeMillis()));

            // Redirect to PayPal
            String paypalUrl = "https://www.paypal.com/cgi-bin/webscr";
            String cmd = "_xclick";
            String businessEmail = config.getString("settings.paypal-email");
            String itemName = "Donation for: " + product.getName();
            String amount = String.valueOf(product.getPrice());
            String currencyCode = config.getString("settings.paypal-currency");
            String notifyUrl = config.getString("settings.website-url") + "/paypal-ipn";
            String returnUrl = config.getString("settings.website-url") + "/success";
            String cancelUrl = config.getString("settings.website-url") + "/cancel";
            String customValue = validation_string;

            String redirectUrl = paypalUrl
                    + "?cmd=" + cmd
                    + "&business=" + businessEmail
                    + "&item_name=" + itemName.replace(" ", "+")
                    + "&amount=" + amount
                    + "&currency_code=" + currencyCode
                    + "&notify_url=" + notifyUrl
                    + "&return=" + returnUrl
                    + "&cancel_return=" + cancelUrl
                    + "&custom=" + customValue;

            response.redirect(redirectUrl);

            return null;
        });

        http.post("/paypal-ipn", (req, res) -> {
            // Get the request body
            String requestBody = req.body();

            // Handle the IPN with the request body
            PayPalIPNHandler.handleIPN(requestBody, database, plugin);

            // You might want to return an appropriate response
            return "IPN Processed";
        });

        // Admin dashboard

        http.get("/admin", (req, res) -> {
            authenticate(req, res);
            Map<String, Object> model = new HashMap<>();
            model.put("currentPage", "home");

            model.put("totalEarnings", Purchase.getTotalEarnings(database));
            model.put("totalEarningsThisMonth", Purchase.getMonthlyTotal(database));
            model.put("purchaseCount", Purchase.getPurchaseCount(database));

            model.put("donations", Purchase.getMonthlyTotal(database));
            model.put("donationGoal", donationGoal);
            model.put("donationGoalPercent", Purchase.getMonthlyTotal(database) * 100 / donationGoal);

            return new ModelAndView(model, "views/admin/home.ftl");
        }, freeMarkerEngine);

        http.get("/admin/categories", (req, res) -> {
            // Authentication and other logic here
            authenticate(req, res);
            Map<String, Object> model = new HashMap<>();
            model.put("currentPage", "categories");
            // Fetch categories from the database and add to the model
            model.put("categories", Category.getCategories(database));
            return new ModelAndView(model, "views/admin/categories.ftl");
        }, freeMarkerEngine);

        http.post("/admin/categories/add", (req, res) -> {
            authenticate(req, res);
            // Extract category details from request
            Category.addCategory(database, req.queryParams("name"), req.queryParams("description"));
            // Redirect back to categories page
            res.redirect("/admin/categories");
            return null;
        });

        http.get("/admin/categories/edit", (req, res) -> {
            authenticate(req, res);
            // Fetch category details from the database based on categoryId
            Map<String, Object> model = new HashMap<>();
            model.put("currentPage", "categories");
            model.put("category", Category.getCategoryById(database, Integer.parseInt(req.queryParams("id"))));
            return new ModelAndView(model, "views/admin/edit_category.ftl");
        }, freeMarkerEngine);

        http.post("/admin/categories/update", (req, res) -> {
            authenticate(req, res);
            // Extract category details from request
            Category.updateCategory(database, Integer.parseInt(req.queryParams("id")), req.queryParams("name"), req.queryParams("description"));
            res.redirect("/admin/categories");
            return null;
        });

        http.post("/admin/categories/delete", (req, res) -> {
            authenticate(req, res);
            // Extract category ID from request
            Category.deleteCategoryById(database, Integer.parseInt(req.queryParams("id")));
            // Redirect back to categories page
            res.redirect("/admin/categories");
            return null;
        });

        // Display Products
        http.get("/admin/products", (req, res) -> {
            authenticate(req, res);
            Map<String, Object> model = new HashMap<>();
            model.put("currentPage", "products");
            model.put("categories", Category.getCategories(database));
            model.put("products", Product.getProducts(database));
            return new ModelAndView(model, "views/admin/products.ftl");
        }, freeMarkerEngine);

        http.post("/admin/products/add", (req, res) -> {
            authenticate(req, res);

            File uploadDirectory = new File(dataLocation.getPath() + "/public/img/");
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            Part filePart = req.raw().getPart("image");

            String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String randomLetters = RandomStringUtils.randomAlphabetic(6);
            String newFileName = randomLetters + "_" + originalFileName;

            String filePath = uploadDirectory.getPath() + "/" + newFileName;
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Product.addProduct(
                    database,
                    Integer.parseInt(req.queryParams("category")),
                    req.queryParams("name"),
                    req.queryParams("description"),
                    Double.parseDouble(req.queryParams("price")),
                    req.queryParams("command"),
                    Boolean.parseBoolean(req.queryParams("featured")),
                    newFileName
            );

            res.redirect("/admin/products");
            return null;
        });

        // Edit Product
        http.get("/admin/products/edit", (req, res) -> {
            authenticate(req, res);
            Map<String, Object> model = new HashMap<>();
            model.put("currentPage", "products");
            model.put("categories", Category.getCategories(database));
            model.put("product", Product.getProductById(database, Integer.parseInt(req.queryParams("id"))
            ));
            return new ModelAndView(model, "views/admin/edit_products.ftl");
        }, freeMarkerEngine);

        // Update Product
        http.post("/admin/products/update", (req, res) -> {
            authenticate(req, res);
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            Product product = Product.getProductById(database, Integer.parseInt(req.queryParams("id")));
            Part filePart = req.raw().getPart("image");
            String newFileName = product.getImageUrl();

            if (filePart != null && filePart.getSize() > 0) {
                File uploadDirectory = new File(dataLocation.getPath() + "/public/img/");
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }

                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String randomLetters = RandomStringUtils.randomAlphabetic(6);
                newFileName = randomLetters + "_" + originalFileName;

                String filePath = uploadDirectory.getPath() + "/" + newFileName;
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Product.updateProduct(
                    database,
                    Integer.parseInt(req.queryParams("id")),
                    Integer.parseInt(req.queryParams("category")),
                    req.queryParams("name"),
                    req.queryParams("description"),
                    Double.parseDouble(req.queryParams("price")),
                    req.queryParams("command"),
                    Boolean.valueOf(req.queryParams("featured")),
                    newFileName
            );
            res.redirect("/admin/products");
            return null;
        });

        http.post("/admin/products/delete", (req, res) -> {
            authenticate(req, res);
            Product.deleteProductById(database, Integer.parseInt(req.queryParams("id")));
            res.redirect("/admin/products");
            return null;
        });

        http.get("/admin/coupons", (req, res) -> {
            authenticate(req, res);
            Map<String, Object> model = new HashMap<>();
            model.put("currentPage", "coupons");
            return new ModelAndView(model, "views/admin/coupons.ftl");
        }, freeMarkerEngine);

        http.get("/admin/purchases", (req, res) -> {
            authenticate(req, res);
            Map<String, Object> model = new HashMap<>();
            model.put("currentPage", "purchases");
            model.put("purchases", Purchase.getPurchases(database));
            model.put("products", Product.getProducts(database));
            return new ModelAndView(model, "views/admin/purchases.ftl");
        }, freeMarkerEngine);

        http.post("/admin/purchases/add", (req, res) -> {
            authenticate(req, res);
            Purchase.addPurchase(database, req.queryParams("email"), Integer.parseInt(req.queryParams("product")), req.queryParams("username"), Double.parseDouble(req.queryParams("price")), RandomStringUtils.randomAlphabetic(8),null);
            // Redirect to the purchases page
            res.redirect("/admin/purchases");
            return null;
        });

        http.get("/admin/purchases/complete", (req, res) -> {
            authenticate(req, res);

            Purchase.completePurchaseAdmin(database, Integer.parseInt(req.queryParams("id")), plugin);

            res.redirect("/admin/purchases");
            return null;
        });

        http.get("/admin/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            // Check if there is a "auth" query parameter and pass its value to the template
            String authStatus = req.queryParams("auth");
            model.put("authFailed", "failed".equals(authStatus));
            return new ModelAndView(model, "views/admin/login.ftl");
        }, freeMarkerEngine);

        http.get("/admin/logout", (req, res) -> {
            // Remove the AuthToken cookie
            //res.removeCookie("AuthToken");
            res.cookie("/", "AuthToken", "none", 60, false);

            // Redirect to the home page
            res.redirect("/");
            return null;
        });

        http.post("/admin/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if (Authentication.sha256(username, password).equals(auth.getAuthToken())) {
                res.cookie("/", "AuthToken", auth.getAuthToken(), 12 * 60 * 60, false);
                res.redirect("/admin");
            } else {
                res.redirect("/admin/login?auth=failed");
            }
            return null;
        });


        // 404 - Not Found route
        http.notFound((req, res) -> {
            res.type("text/html");
            return freeMarkerEngine.render(new ModelAndView(new HashMap<>(), "views/system/404.ftl"));
        });

        http.init();

    }

    private void setupStaticFiles(File dataLocation) {
        File publicDir = new File(dataLocation, "public");
        if (!publicDir.exists()) {
            publicDir.mkdirs();
        }

        // Configure Spark to serve static files from /public
        http.staticFiles.externalLocation(publicDir.getAbsolutePath());
    }
}
