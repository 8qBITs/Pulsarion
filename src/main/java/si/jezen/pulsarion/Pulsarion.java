package si.jezen.pulsarion;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import si.jezen.pulsarion.web.Server;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.jar.JarFile;

public final class Pulsarion extends JavaPlugin {

    private Connection database;
    private Server server;

    private Pulsarion instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResourceFiles();

        String url = "jdbc:sqlite:" + new File(getDataFolder(), "pulsarion.db").getAbsolutePath();

        try {
            database = DriverManager.getConnection(url);
        } catch (SQLException e) {
            getLogger().severe(e.getMessage());
        }

        initDB();

        server = new Server(getConfig(), getDataFolder(), database, instance);
        server.serve();
    }

    @Override
    public void onDisable() {
        server.stop();
        // Plugin shutdown logic
    }
    private void initDB() {
        try {
            String sqlCategories = "CREATE TABLE IF NOT EXISTS categories (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + " name TEXT NOT NULL,\n"
                    + " description TEXT\n"
                    + ");";
            String sqlProducts = "CREATE TABLE IF NOT EXISTS products (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + " category INTEGER NOT NULL,\n"
                    + " name TEXT NOT NULL,\n"
                    + " description TEXT,\n"
                    + " price REAL NOT NULL,\n"
                    + " command TEXT,\n"
                    + " featured BOOLEAN DEFAULT FALSE,\n"
                    + " imageUrl TEXT DEFAULT 'cat.png',\n"
                    + " FOREIGN KEY (category) REFERENCES categories(id)\n"
                    + ");";
            String sqlPurchases = "CREATE TABLE IF NOT EXISTS purchases (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + " email TEXT NOT NULL,\n"
                    + " product INTEGER NOT NULL,\n"
                    + " username TEXT NOT NULL,\n"
                    + " price REAL NOT NULL,\n"
                    + " validation TEXT,  -- New field for validation hash\n"
                    + " timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,\n"
                    + " FOREIGN KEY (product) REFERENCES products(id)\n"
                    + ");";
            String sqlCoupons = "CREATE TABLE IF NOT EXISTS coupons (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + " percentage INTEGER NOT NULL,\n"
                    + " enabled BOOLEAN NOT NULL,\n"
                    + " max_uses INTEGER NOT NULL,\n"
                    + " current_uses INTEGER NOT NULL DEFAULT 0\n"
                    + ");";

            Statement stmt = database.createStatement();
            stmt.execute(sqlCategories);
            stmt.execute(sqlProducts);
            stmt.execute(sqlPurchases);
            stmt.execute(sqlCoupons);
        } catch (SQLException e) {
            getLogger().severe(e.getMessage());
        }
    }

    private void saveResourceFiles() {
        File jarFile = this.getFile(); // Get the plugin's JAR file
        File dataFolder = this.getDataFolder();

        try (JarFile jar = new JarFile(jarFile)) {
            jar.stream().forEach(entry -> {
                try {
                    String name = entry.getName();
                    if (name.startsWith("views/") || name.startsWith("public/")) {
                        File outFile = new File(dataFolder, name);
                        if (!entry.isDirectory() && !outFile.exists()) {
                            outFile.getParentFile().mkdirs(); // Ensure parent directories exist
                            try (InputStream in = jar.getInputStream(entry);
                                 OutputStream out = new FileOutputStream(outFile)) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    getLogger().warning("Failed to copy resource file: " + entry.getName());
                }
            });
        } catch (Exception e) {
            getLogger().severe("Failed to open plugin JAR file.");
        }
    }

    public Pulsarion getInstance() {
        return instance;
    }
}
