package fr.nemesis07.ankhiastaff.managers;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.infractions.Infraction;
import fr.nemesis07.ankhiastaff.infractions.InfractionType;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {
    private HikariDataSource dataSource = null;
    private boolean isInitializedSuccessfully = false;

    public DatabaseManager(boolean enabled, String url, String username, String password) {
        if (enabled && url != null && username != null && password != null) {
            AnkhiaStaff.getInstance().getLogger().info("Using external database for warnings and reports.");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            try {
                this.dataSource = new HikariDataSource(config);
                Connection conn = this.dataSource.getConnection();
                this.createTables(conn);
                conn.close();
                this.isInitializedSuccessfully = true;
            } catch (Exception var7) {
                AnkhiaStaff.getInstance().getLogger().info("Failed to initialize database connection: " + var7.getMessage());
                this.isInitializedSuccessfully = false;
                this.dataSource = null;
            }

        } else {
            AnkhiaStaff.getInstance().getLogger().info("No database information provided. Using local configuration.");
        }
    }

    private void createTables(Connection conn) throws SQLException {
        String createInfractionsTableQuery = "CREATE TABLE IF NOT EXISTS staffmodex_infractions (id VARCHAR(36) PRIMARY KEY,accused_uuid VARCHAR(36) NOT NULL,type VARCHAR(10) NOT NULL,timestamp VARCHAR(20) NOT NULL,reporter_uuid VARCHAR(36) NOT NULL,reporter_name VARCHAR(16) NOT NULL,reason VARCHAR(255) NOT NULL)";
        String createIpsQuery = "CREATE TABLE IF NOT EXISTS staffmodex_ips (id VARCHAR(36) PRIMARY KEY,ip VARCHAR(45))";
        PreparedStatement stmt = conn.prepareStatement(createInfractionsTableQuery);

        try {
            stmt.executeUpdate();
        } catch (Throwable var10) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable var8) {
                    var10.addSuppressed(var8);
                }
            }

            throw var10;
        }

        if (stmt != null) {
            stmt.close();
        }

        stmt = conn.prepareStatement(createIpsQuery);

        try {
            stmt.executeUpdate();
        } catch (Throwable var9) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable var7) {
                    var9.addSuppressed(var7);
                }
            }

            throw var9;
        }

        if (stmt != null) {
            stmt.close();
        }

    }

    public boolean isInitializedSuccessfully() {
        return this.isInitializedSuccessfully && this.dataSource != null;
    }

    public void saveInfraction(Infraction infraction) {
        String query = "INSERT INTO staffmodex_infractions (accused_uuid, type, timestamp, reporter_uuid, reporter_name, reason, id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = this.dataSource.getConnection();

            try {
                PreparedStatement stmt = conn.prepareStatement(query);

                try {
                    stmt.setString(1, infraction.getAccusedUUID().toString());
                    stmt.setString(2, infraction.getType().toString());
                    stmt.setString(3, infraction.getTimestamp());
                    stmt.setString(4, infraction.getReporterUUID().toString());
                    stmt.setString(5, infraction.getReporterName());
                    stmt.setString(6, infraction.getReason());
                    stmt.setString(7, infraction.getId().toString());
                    stmt.executeUpdate();
                } catch (Throwable var9) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Throwable var10) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                }

                throw var10;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var11) {
            this.close();
            var11.printStackTrace();
        }

    }

    public void loadInfractions(StaffPlayer staffPlayer) {
        String query = "SELECT id, type, timestamp, reporter_uuid, reporter_name, reason FROM staffmodex_infractions WHERE accused_uuid = ?";

        try {
            Connection conn = this.dataSource.getConnection();

            try {
                PreparedStatement stmt = conn.prepareStatement(query);

                try {
                    stmt.setString(1, staffPlayer.getUUID().toString());
                    ResultSet resultSet = stmt.executeQuery();

                    try {
                        while(resultSet.next()) {
                            try {
                                String typeStr = resultSet.getString("type");
                                String timestampStr = resultSet.getString("timestamp");
                                String reason = resultSet.getString("reason");
                                InfractionType type = InfractionType.valueOf(typeStr);
                                UUID reporterUUID = UUID.fromString(resultSet.getString("reporter_uuid"));
                                String reporterName = resultSet.getString("reporter_name");
                                UUID id = UUID.fromString(resultSet.getString("id"));
                                switch(type) {
                                    case REPORT:
                                        staffPlayer.getReports().addInfraction(new Infraction(id, timestampStr, reporterName, reason, staffPlayer.getUUID(), reporterUUID, type));
                                        break;
                                    case WARNING:
                                        staffPlayer.getWarnings().addInfraction(new Infraction(id, timestampStr, reporterName, reason, staffPlayer.getUUID(), reporterUUID, type));
                                }
                            } catch (Exception var16) {
                                AnkhiaStaff.getInstance().getLogger().severe("Error while loading infraction from config");
                                var16.printStackTrace();
                            }
                        }
                    } catch (Throwable var17) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var15) {
                                var17.addSuppressed(var15);
                            }
                        }

                        throw var17;
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Throwable var18) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var14) {
                            var18.addSuppressed(var14);
                        }
                    }

                    throw var18;
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Throwable var19) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var13) {
                        var19.addSuppressed(var13);
                    }
                }

                throw var19;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var20) {
            this.close();
            var20.printStackTrace();
        }

    }

    public void close() {
        if (this.dataSource != null) {
            this.dataSource.close();
            this.dataSource = null;
        }

    }

    public void saveIP(UUID uuid, String ip) {
        String query = "INSERT INTO staffmodex_ips (id, ip) VALUES (?, ?) ON DUPLICATE KEY UPDATE ip = VALUES(ip)";

        try {
            Connection conn = this.dataSource.getConnection();

            try {
                PreparedStatement stmt = conn.prepareStatement(query);

                try {
                    stmt.setString(1, uuid.toString());
                    stmt.setString(2, ip);
                    stmt.executeUpdate();
                } catch (Throwable var10) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var9) {
                            var10.addSuppressed(var9);
                        }
                    }

                    throw var10;
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Throwable var11) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var8) {
                        var11.addSuppressed(var8);
                    }
                }

                throw var11;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var12) {
            this.close();
            var12.printStackTrace();
        }

    }

    public void loadIP(StaffPlayer staffPlayer) {
        String query = "SELECT id, ip FROM staffmodex_ips WHERE id = ?";

        try {
            Connection conn = this.dataSource.getConnection();

            try {
                PreparedStatement stmt = conn.prepareStatement(query);

                try {
                    stmt.setString(1, staffPlayer.getUUID().toString());
                    ResultSet resultSet = stmt.executeQuery();

                    try {
                        while(resultSet.next()) {
                            try {
                                String ip = resultSet.getString("ip");
                                staffPlayer.setIP(ip);
                            } catch (Exception var11) {
                                AnkhiaStaff.getInstance().getLogger().severe("Error while loading infraction from config");
                                var11.printStackTrace();
                            }
                        }
                    } catch (Throwable var12) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var10) {
                                var12.addSuppressed(var10);
                            }
                        }

                        throw var12;
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Throwable var13) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var9) {
                            var13.addSuppressed(var9);
                        }
                    }

                    throw var13;
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Throwable var14) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var8) {
                        var14.addSuppressed(var8);
                    }
                }

                throw var14;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var15) {
            this.close();
            var15.printStackTrace();
        }

    }
}