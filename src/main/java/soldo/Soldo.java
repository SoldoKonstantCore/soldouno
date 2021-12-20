/** ****************************************************************************
 * Copyright © 2013-2016 The Nxt Core Developers.                             *
 *                                                                            *
 * See the AUTHORS.txt, DEVELOPER-AGREEMENT.txt and LICENSE.txt files at      *
 * the top-level directory of this distribution for the individual copyright  *
 * holder information and the developer policies on copyright and licensing.  *
 *                                                                            *
 * Unless otherwise agreed in a custom licensing agreement, no part of the    *
 * Nxt software, including this file, may be copied, modified, propagated,    *
 * or distributed except according to the terms contained in the LICENSE.txt  *
 * file.                                                                      *
 *                                                                            *
 * Removal or modification of this copyright notice is prohibited.            *
 *                                                                            *
 ***************************************************************************** */
package soldo;

import soldo.util.ThreadPool;
import soldo.util.Version;
import soldo.util.Convert;
import soldo.util.Time;
import soldo.util.Logger;
import soldo.addons.AddOns;
import soldo.crypto.Crypto;
import soldo.env.DirProvider;
import soldo.env.RuntimeEnvironment;
import soldo.env.RuntimeMode;
import soldo.env.ServerStatus;
import soldo.http.API;
import soldo.http.APIProxy;
import soldo.peer.Peers;
import soldo.user.Users;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessControlException;
import java.util.*;

public final class Soldo {

    private static SoftMG softMG_;

    public static SoftMG softMG() {
        return softMG_;
    }

    public static final String MINIMAL_COMPATIBLE_VERSION = "0.0.0.1"; 
    public static final String VERSION = "0.0.0.1";
    public static final String APPLICATION = "SOL";

    private static volatile Time time = new Time.EpochTime();

    public static final String SOLDO_DEFAULT_PROPERTIES = "soldo.default.properties";
    public static final String SOLDO_PROPERTIES = "soldo.properties";
    public static final String SOLDO_INSTALLER_PROPERTIES = "soldo.installer.properties";
    public static final String CONFIG_DIR = "conf";

    private static final RuntimeMode runtimeMode;
    private static final DirProvider dirProvider;

    private static boolean performRescan = false;

    private static final Properties defaultProperties = new Properties();
    static {
        redirectSystemStreams("out");
        redirectSystemStreams("err");
        System.out.println("Initializing Soldo server version " + Soldo.VERSION);
        printCommandLineArguments();
        runtimeMode = RuntimeEnvironment.getRuntimeMode();
        System.out.printf("Runtime mode %s\n", runtimeMode.getClass().getName());
        dirProvider = RuntimeEnvironment.getDirProvider();
        System.out.println("User home folder " + dirProvider.getUserHomeDir());
        loadProperties(defaultProperties, SOLDO_DEFAULT_PROPERTIES, true);
        if (!VERSION.equals(Soldo.defaultProperties.getProperty("soldo.version"))) {
            throw new RuntimeException("Using an soldo.default.properties file from a version other than " + VERSION + " is not supported!!!");
        }
    }

    private static void redirectSystemStreams(String streamName) {
        String isStandardRedirect = System.getProperty("soldo.redirect.system." + streamName);
        Path path = null;
        if (isStandardRedirect != null) {
            try {
                path = Files.createTempFile("soldo.system." + streamName + ".", ".log");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            String explicitFileName = System.getProperty("soldo.system." + streamName);
            if (explicitFileName != null) {
                path = Paths.get(explicitFileName);
            }
        }
        if (path != null) {
            try {
                PrintStream stream = new PrintStream(Files.newOutputStream(path));
                if (streamName.equals("out")) {
                    System.setOut(new PrintStream(stream));
                } else {
                    System.setErr(new PrintStream(stream));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Properties properties = new Properties(defaultProperties);

    static {
        loadProperties(properties, SOLDO_INSTALLER_PROPERTIES, true);
        loadProperties(properties, SOLDO_PROPERTIES, false);
    }

    public static Properties loadProperties(Properties properties, String propertiesFile, boolean isDefault) {
        try {
            // Load properties from location specified as command line parameter
            String configFile = System.getProperty(propertiesFile);

            if (configFile != null) {
                System.out.printf("Loading %s from %s\n", propertiesFile, configFile);
                try (InputStream fis = new FileInputStream(configFile)) {
                    properties.load(fis);
                    return properties;
                } catch (IOException e) {
                    throw new IllegalArgumentException(String.format("Error loading %s from %s", propertiesFile, configFile));
                }
            } else {
                try (InputStream is = ClassLoader.getSystemResourceAsStream(propertiesFile)) {
                  
                    if (is != null) {
                        System.out.printf("Loading %s from classpath\n", propertiesFile);
                        properties.load(is);
                        if (isDefault) {
                            return properties;
                        }
                    }
                 
                  if (!dirProvider.isLoadPropertyFileFromUserDir()) {
                     return properties;
                  }
                    String homeDir = dirProvider.getUserHomeDir();
                    if (!Files.isReadable(Paths.get(homeDir))) {
                        System.out.printf("Creating dir %s\n", homeDir);
                        try {
                            Files.createDirectory(Paths.get(homeDir));
                        } catch (Exception e) {
                            if (!(e instanceof NoSuchFileException)) {
                                throw e;
                            }
                            // Fix for WinXP and 2003 which does have a roaming sub folder
                            Files.createDirectory(Paths.get(homeDir).getParent());
                            Files.createDirectory(Paths.get(homeDir));
                        }
                    }
                    Path confDir = Paths.get(homeDir, CONFIG_DIR);
                    if (!Files.isReadable(confDir)) {
                        System.out.printf("Creating dir %s\n", confDir);
                        Files.createDirectory(confDir);
                    }
                    Path propPath = Paths.get(confDir.toString()).resolve(Paths.get(propertiesFile));
                    if (Files.isReadable(propPath)) {
                        System.out.printf("Loading %s from dir %s\n", propertiesFile, confDir);
                        properties.load(Files.newInputStream(propPath));
                    } else {
                        System.out.printf("Creating property file %s\n", propPath);
                        Files.createFile(propPath);
                        Files.write(propPath, Convert.toBytes("# use this file for workstation specific " + propertiesFile));
                    }
                    return properties;
                } catch (IOException e) {
                    throw new IllegalArgumentException("Error loading " + propertiesFile, e);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // make sure we log this exception
            throw e;
        }
    }

    private static void printCommandLineArguments() {
        try {
            List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            if (inputArguments != null && inputArguments.size() > 0) {
                System.out.println("Command line arguments");
            } else {
                return;
            }
            inputArguments.forEach(System.out::println);
        } catch (AccessControlException e) {
            System.out.println("Cannot read input arguments " + e.getMessage());
        }
    }

    public static int getIntProperty(String name) {
        return getIntProperty(name, 0);
    }

    public static int getIntProperty(String name, int defaultValue) {
        try {
            int result = Integer.parseInt(properties.getProperty(name));
            Logger.logMessage(name + " = \"" + result + "\"");
            return result;
        } catch (NumberFormatException e) {
            Logger.logMessage(name + " not defined or not numeric, using default value " + defaultValue);
            return defaultValue;
        }
    }

    public static String getStringProperty(String name) {
        return getStringProperty(name, null, false);
    }

    public static String getStringProperty(String name, String defaultValue) {
        return getStringProperty(name, defaultValue, false);
    }

    public static String getStringProperty(String name, String defaultValue, boolean doNotLog) {
        return getStringProperty(name, defaultValue, doNotLog, null);
    }

    public static String getStringProperty(String name, String defaultValue, boolean doNotLog, String encoding) {
        String value = properties.getProperty(name);
        if (value != null && !"".equals(value)) {
            Logger.logMessage(name + " = \"" + (doNotLog ? "{not logged}" : value) + "\"");
        } else {
            Logger.logMessage(name + " not defined");
            value = defaultValue;
        }
        if (encoding == null || value == null) {
            return value;
        }
        try {
            return new String(value.getBytes("ISO-8859-1"), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getStringListProperty(String name) {
        String value = getStringProperty(name);
        if (value == null || value.length() == 0) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        for (String s : value.split(";")) {
            s = s.trim();
            if (s.length() > 0) {
                result.add(s);
            }
        }
        return result;
    }

    public static boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = properties.getProperty(name);
        if (Boolean.TRUE.toString().equals(value)) {
            Logger.logMessage(name + " = \"true\"");
            return true;
        } else if (Boolean.FALSE.toString().equals(value)) {
            Logger.logMessage(name + " = \"false\"");
            return false;
        }
        Logger.logMessage(name + " not defined, using default " + defaultValue);
        return defaultValue;
    }

    public static Blockchain getBlockchain() {
        return BlockchainImpl.getInstance();
    }

    public static BlockchainProcessor getBlockchainProcessor() {
        return BlockchainProcessorImpl.getInstance();
    }

    public static TransactionProcessor getTransactionProcessor() {
        return TransactionProcessorImpl.getInstance();
    }

    public static Transaction.Builder newTransactionBuilder(byte[] senderPublicKey, long amountNQT, long feeNQT, short deadline, Attachment attachment) {
        return new TransactionImpl.BuilderImpl((byte) 1, senderPublicKey, amountNQT, feeNQT, deadline, (Attachment.AbstractAttachment) attachment);
    }

    public static Transaction.Builder newTransactionBuilder(byte[] transactionBytes) throws SOLException.NotValidException {
        return TransactionImpl.newTransactionBuilder(transactionBytes);
    }

    public static Transaction.Builder newTransactionBuilder(JSONObject transactionJSON) throws SOLException.NotValidException {
        return TransactionImpl.newTransactionBuilder(transactionJSON);
    }

    public static Transaction.Builder newTransactionBuilder(byte[] transactionBytes, JSONObject prunableAttachments) throws SOLException.NotValidException {
        return TransactionImpl.newTransactionBuilder(transactionBytes, prunableAttachments);
    }

    public static int getEpochTime() {
        return time.getTime();
    }


    static void setTime(Time time) {
        Soldo.time = time;
    }

    public static void main(String[] args) {

        
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(Soldo::shutdown));
            init();
        } catch (Throwable t) {
            System.out.println("Fatal error: " + t.toString());
            t.printStackTrace();
        }
    }

    public static void init(Properties customProperties) {
        properties.putAll(customProperties);
        init();
    }

    public static void init() {
        Init.init();
    }

    public static void shutdown() {
        Logger.logShutdownMessage("Shutting down...");
        AddOns.shutdown();
        API.shutdown();
        Users.shutdown();
        ThreadPool.shutdown();
        BlockchainProcessorImpl.getInstance().shutdown();
        Peers.shutdown();
        Db.shutdown();
        Logger.logShutdownMessage("Soldo server " + VERSION + " stopped.");
        Logger.shutdown();
        Soldo.softMG().shutdown();
        runtimeMode.shutdown();
    }

    private static class Init {

        private static volatile boolean initialized = false;

        static {
            try {
                long startTime = System.currentTimeMillis();
                Logger.init();
                setSystemProperties();
                logSystemProperties();
                runtimeMode.init();
                Thread secureRandomInitThread = initSecureRandom();
                setServerStatus(ServerStatus.BEFORE_DATABASE, null);
                Db.init();
                softMG_ = new SoftMGImpl(Db.SoftMG_DB_URL, Db.SoftMG_DB_USERNAME, Db.SoftMG_DB_PASSWORD);
                setServerStatus(ServerStatus.AFTER_DATABASE, null);
                TransactionProcessorImpl.getInstance();
                BlockchainProcessorImpl.getInstance();
                Account.init();
                AccountLedger.init();
                Alias.init();
                PrunableMessage.init();
                Peers.init();
                APIProxy.init();
                AddOns.init();
                API.init();
                Users.init();
                DebugTrace.init();
                softMG_.init();
                int timeMultiplier = (Constants.isTestnet && Constants.isOffline) ? Math.max(Soldo.getIntProperty("soldo.timeMultiplier"), 1) : 1;
                ThreadPool.start(timeMultiplier);
                if (timeMultiplier > 1) {
                    setTime(new Time.FasterTime(Math.max(getEpochTime(), Soldo.getBlockchain().getLastBlock().getTimestamp()), timeMultiplier));
                    Logger.logMessage("TIME WILL FLOW " + timeMultiplier + " TIMES FASTER!");
                }
                try {
                    secureRandomInitThread.join(10000);
                } catch (InterruptedException ignore) {
                }
                testSecureRandom();
                long currentTime = System.currentTimeMillis();
                Logger.logMessage("Initialization took " + (currentTime - startTime) / 1000 + " seconds");
                Logger.logMessage("SOLDO server " + VERSION + " started successfully.");
                Logger.logMessage("Copyright © 2013-2016 The Nxt Core Developers.");
                Logger.logMessage("Copyright © 2016-2017 Jelurida IP B.V.");
                Logger.logMessage("Distributed under the Jelurida Public License version 1.0 for the N.x.t Public Blockchain Platform, with ABSOLUTELY NO WARRANTY.");
                if (API.getWelcomePageUri() != null) {
                    Logger.logMessage("Client UI is at " + API.getWelcomePageUri());
                }
                setServerStatus(ServerStatus.STARTED, API.getWelcomePageUri());

                if (isDesktopApplicationEnabled()) {
                    launchDesktopApplication();
                }
                if (Constants.isTestnet) {
                    Logger.logMessage("RUNNING ON TESTNET - DO NOT USE REAL ACCOUNTS!");
                }

            } catch (Exception e) {
                Logger.logErrorMessage(e.getMessage(), e);
                runtimeMode.alert(e.getMessage() + "\n" +
                        "See additional information in " + dirProvider.getLogFileDir() + System.getProperty("file.separator") + "soldo.log");
                System.exit(1);
            }
        }

        private static void init() {
            if (initialized) {
                throw new RuntimeException("soldo.init has already been called");
            }
            initialized = true;
        }

        private Init() {
        } // never

    }

    private static void setSystemProperties() {
        // Override system settings that the user has define in soldo.properties file.
        String[] systemProperties = new String[]{
            "socksProxyHost",
            "socksProxyPort",};

        for (String propertyName : systemProperties) {
            String propertyValue;
            if ((propertyValue = getStringProperty(propertyName)) != null) {
                System.setProperty(propertyName, propertyValue);
        }
      }
    }

    private static void logSystemProperties() {
        String[] loggedProperties = new String[]{
            "java.version",
            "java.vm.version",
            "java.vm.name",
            "java.vendor",
            "java.vm.vendor",
            "java.home",
            "java.library.path",
            "java.class.path",
            "os.arch",
            "sun.arch.data.model",
            "os.name",
            "file.encoding",
            "java.security.policy",
            "java.security.manager",
            RuntimeEnvironment.RUNTIME_MODE_ARG,
            RuntimeEnvironment.DIRPROVIDER_ARG
        };
        for (String property : loggedProperties) {
            Logger.logDebugMessage(String.format("%s = %s", property, System.getProperty(property)));
        }
        Logger.logDebugMessage(String.format("availableProcessors = %s", Runtime.getRuntime().availableProcessors()));
        Logger.logDebugMessage(String.format("maxMemory = %s", Runtime.getRuntime().maxMemory()));
        Logger.logDebugMessage(String.format("processId = %s", getProcessId()));
    }

    private static Thread initSecureRandom() {
        Thread secureRandomInitThread = new Thread(() -> Crypto.getSecureRandom().nextBytes(new byte[1024]));
        secureRandomInitThread.setDaemon(true);
        secureRandomInitThread.start();
        return secureRandomInitThread;
    }

    private static void testSecureRandom() {
        Thread thread = new Thread(() -> Crypto.getSecureRandom().nextBytes(new byte[1024]));
        thread.setDaemon(true);
        thread.start();
        try {
            thread.join(2000);
            if (thread.isAlive()) {
                throw new RuntimeException("SecureRandom implementation too slow!!! "
                        + "Install haveged if on linux, or set soldo.useStrongSecureRandom=false.");
            }
        } catch (InterruptedException ignore) {
        }
    }

    public static String getProcessId() {
        String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
        if (runtimeName == null) {
            return "";
        }
        String[] tokens = runtimeName.split("@");
        if (tokens.length == 2) {
            return tokens[0];
        }
        return "";
    }

    public static String getDbDir(String dbDir) {
        return dirProvider.getDbDir(dbDir);
    }

    public static void updateLogFileHandler(Properties loggingProperties) {
        dirProvider.updateLogFileHandler(loggingProperties);
    }

    public static String getUserHomeDir() {
        return dirProvider.getUserHomeDir();
    }

    public static File getConfDir() {
        return dirProvider.getConfDir();
    }

    private static void setServerStatus(ServerStatus status, URI wallet) {
        runtimeMode.setServerStatus(status, wallet, dirProvider.getLogFileDir());
    }

    public static void setPerformRescan(boolean value) {
        performRescan = value;
    }

    public static boolean shouldPerformRescan() {
        return performRescan;
    }

    public static boolean isDesktopApplicationEnabled() {
        return RuntimeEnvironment.isDesktopApplicationEnabled() && Soldo.getBooleanProperty("soldo.launchDesktopApplication");
    }

    private static void launchDesktopApplication() {
        runtimeMode.launchDesktopApplication();
    }

    public static boolean isCompatiblePeerVersion(String otherVersion) {
        return Soldo.VERSION.equals(otherVersion) || !Version.MINIMAL_COMPATIBLE.isNewerThen(otherVersion);
    }
    
    private Soldo() {
    } // never

}
