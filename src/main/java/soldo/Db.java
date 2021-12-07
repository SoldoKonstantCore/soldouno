/******************************************************************************
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
 ******************************************************************************/

package soldo;

import soldo.db.BasicDb;
import soldo.db.TransactionalDb;

public final class Db {
    
    public static final String PREFIX = Constants.isTestnet ? "soldo.testDb" : "soldo.db";
    public static final String PREFIX_SoftMg = Constants.isTestnet ? "soldo.testSoftMgDb" : "soldo.dbSoftMg";
    
    public static final String SoftMG_DB_URL = String.format("jdbc:%s:%s;%s", "h2", Soldo.getDbDir("./soldo_db/softMgsld"), "DB_CLOSE_ON_EXIT=FALSE;MVCC=TRUE;MV_STORE=FALSE");
    public static final String SoftMG_DB_USERNAME = Soldo.getStringProperty(PREFIX_SoftMg + "Username");
    public static final String SoftMG_DB_PASSWORD = Soldo.getStringProperty(PREFIX_SoftMg + "Password");
    
    public static final TransactionalDb db = new TransactionalDb(new BasicDb.DbProperties()
            .maxCacheSize(Soldo.getIntProperty("soldo.dbCacheKB"))
            .dbUrl(Soldo.getStringProperty(PREFIX + "Url"))
            .dbType(Soldo.getStringProperty(PREFIX + "Type"))
            .dbDir(Soldo.getStringProperty(PREFIX + "Dir"))
            .dbParams(Soldo.getStringProperty(PREFIX + "Params"))
            .dbUsername(Soldo.getStringProperty(PREFIX + "Username"))
            .dbPassword(Soldo.getStringProperty(PREFIX + "Password", null, true))
            .maxConnections(Soldo.getIntProperty("soldo.maxDbConnections"))
            .loginTimeout(Soldo.getIntProperty("soldo.dbLoginTimeout"))
            .defaultLockTimeout(Soldo.getIntProperty("soldo.dbDefaultLockTimeout") * 1000)
            .maxMemoryRows(Soldo.getIntProperty("soldo.dbMaxMemoryRows"))
    );

    public static void init() {
        db.init(new soldoDbVersion());
    }

    static void shutdown() {
        db.shutdown();
    }

    private Db() {} // never

}
