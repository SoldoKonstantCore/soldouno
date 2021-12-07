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


import java.math.BigInteger;
import java.util.*;

public final class Constants {

    public static final boolean isTestnet = Soldo.getBooleanProperty("soldo.isTestnet");
    public static final boolean isOffline = Soldo.getBooleanProperty("soldo.isOffline");
    public static final boolean isLightClient = Soldo.getBooleanProperty("soldo.isLightClient");
    public static final String customLoginWarning = Soldo.getStringProperty("soldo.customLoginWarning", null, false, "UTF-8");

    public static final int MAX_NUMBER_OF_TRANSACTIONS = 100;
    public static final int MAX_NAGRADNIH = MAX_NUMBER_OF_TRANSACTIONS * 70 / 100;  
    public static final int MAX_PROSTIH = MAX_NUMBER_OF_TRANSACTIONS * 30 / 100;   
    
    public static final int MIN_TRANSACTION_SIZE = 400;
    public static final int MAX_PAYLOAD_LENGTH = MAX_NUMBER_OF_TRANSACTIONS * MIN_TRANSACTION_SIZE; 
    
    
    
    public static final int MAX_TRANSACTION_PAYLOAD_LENGTH = 1536;
    public static final long MAX_BALANCE_SLD = 15000000000L; 
    public static final long EndEmissions = 150000000000L; 
    public static final long ONE_SLD = 1000000L; 
    public static final long MAX_BALANCE_centesimo = MAX_BALANCE_SLD * ONE_SLD;
    public static final long MAX_full_BALANCE_centesimo = EndEmissions* ONE_SLD; 

    public static final int BLOCK_TIME = 60;
    public static final long INITIAL_BASE_TARGET = BigInteger.valueOf(2).pow(63).divide(BigInteger.valueOf(BLOCK_TIME * MAX_BALANCE_SLD)).longValue(); 

public static final int fasterer_GENERACII_BLOCKOV = 1;

public static final int MIN_BLOCKTIME_DELTA = 10;
public static final int MAX_BLOCKTIME_DELTA = 30;

public static final int BASE_TARGET_GAMMA = 72;
 
public static long getMAX_BASE_TARGET(int height) {
        long max= getINITIAL_BASE_TARGET( height)* ( (Account.getAccount(Genesis.CREATOR_ID).getBalanceNQT()*(-1)/ONE_SLD) /100);
        return  max;
}
public static long getMIN_BASE_TARGET(int height) {
    long min = getINITIAL_BASE_TARGET( height)* 9 / 10;
    return min;
}

    public static final int HOLD_ENABLE_HEIGHT = 1440;  
    public static final int HOLD_RANGE = 100000;     
    public static final long HOLD_BALANCE_MIN = 1100L;  
    public static final long HOLD_BALANCE_MAX = 110000L; 

    
    public static final int ENABLE_BLOCK_VERSION_PREVALIDATION = 71; 
    public static final long MAX_BALANCE_Stop_mg_centesimo =  100000000000L;  
    public static final long MAX_softMG_PAYOUT_centesimo   =  10000000000L; 
    

    
    public static final long MIN_FEE_NQT = 150000L;
    public static final int MAX_ROLLBACK = Math.max(Soldo.getIntProperty("soldo.maxRollback"), 720);
    public static final int TRIM_FREQUENCY = Math.max(Soldo.getIntProperty("soldo.trimFrequency"), 1);
    public static final int GUARANTEED_BALANCE_CONFIRMATIONS = isTestnet ? Soldo.getIntProperty("soldo.testnetGuaranteedBalanceConfirmations", 1440) : 1440;
    public static final int LEASING_DELAY = isTestnet ? Soldo.getIntProperty("soldo.testnetLeasingDelay", 1440) : 1440;
    public static final long MIN_FORGING_BALANCE_NQT = 1000 * ONE_SLD;

    public static final int MAX_TIMEDRIFT = 15;

    
    public static final int FORGING_DELAY = Math.min(MAX_TIMEDRIFT - 1, Soldo.getIntProperty("nxt.forgingDelay"));
    public static final int FORGING_SPEEDUP = Soldo.getIntProperty("nxt.forgingSpeedup");
    public static final int BATCH_COMMIT_SIZE = Soldo.getIntProperty("soldo.batchCommitSize", 100);

    public static final int MAX_ALIAS_URI_LENGTH = 512;
    public static final int MAX_ALIAS_LENGTH = 100;

    public static final int MAX_ARBITRARY_MESSAGE_LENGTH = 160;
    public static final int MAX_ENCRYPTED_MESSAGE_LENGTH = 160 + 16;

    public static final int MAX_PRUNABLE_MESSAGE_LENGTH = 512; 
    public static final int MAX_PRUNABLE_ENCRYPTED_MESSAGE_LENGTH = 512;  

    public static final int MIN_PRUNABLE_LIFETIME = isTestnet ? 1440 * 60 : 14 * 600 * 60;
    public static final int MAX_PRUNABLE_LIFETIME;
    public static final boolean ENABLE_PRUNING;

    private static List<String> blacklistedBlocksList = Soldo.getStringListProperty("soldo.blacklist.blocks");
    
    static {
        int maxPrunableLifetime = Soldo.getIntProperty("soldo.maxPrunableLifetime");
        ENABLE_PRUNING = maxPrunableLifetime >= 0;
        MAX_PRUNABLE_LIFETIME = ENABLE_PRUNING ? Math.max(maxPrunableLifetime, MIN_PRUNABLE_LIFETIME) : Integer.MAX_VALUE;
        List<String> backup = blacklistedBlocksList;
        blacklistedBlocksList = new ArrayList<String>();
        blacklistedBlocksList.addAll(backup);

    }

    public static Set<String> getBlacklistedBlocks() {
        return new HashSet<>(blacklistedBlocksList);
    }
    
    public static final boolean INCLUDE_EXPIRED_PRUNABLE = Soldo.getBooleanProperty("soldo.includeExpiredPrunable");

    public static final boolean MEASURE_TRIMMING_TIME = Soldo.getBooleanProperty("soldo.measureTrimmingTime");

    public static final int MAX_ACCOUNT_NAME_LENGTH = 100;
    public static final int MAX_ACCOUNT_DESCRIPTION_LENGTH = 512;

    public static final int TRANSPARENT_FORGING_BLOCK = 1440; 
    public static final int TRANSPARENT_FORGING_BLOCK_firstforg = 100;
    public static final int TRANSPARENT_FORGING_BLOCK_3 = 1450; 
                                                                      
   public static final int TRANSPARENT_FORGING_BLOCK_8 = 1460;
    public static final int FIRST_FORK_BLOCK = 1500; 

     public static final int ENFORCE_FULL_TRANSACTION_VALIDATION = 1540; 
    
    
    public static final int FORBID_FORGING_WITH_YOUNG_PUBLIC_KEY = 3001;
    public static final int NQT_BLOCK = isTestnet ? 0 : 0;  
    public static final int MAX_REFERENCED_TRANSACTION_TIMESPAN = 60 * 600 * 60;



   

    public static final int LAST_KNOWN_BLOCK = 1440;

    public static final int[] MIN_VERSION = new int[] {0, 0, 0, 1};
    public static final int[] MIN_PROXY_VERSION = new int[] {2, 0, 0, 0};

    public static final boolean correctInvalidFees = Soldo.getBooleanProperty("soldo.correctInvalidFees");

    public static final int maxBlockchainHeight = Soldo.getIntProperty("soldo.maxBlockchainHeight");
    public static final boolean limitBlockchainHeight = maxBlockchainHeight > 0;
    public static final boolean SERVE_ONLY_LATEST_TRANSACTIONS = Soldo.getBooleanProperty("soldo.serveOnlyLatestTransactions", false);

    // --------[INIT #A]-------
    public static final long EPOCH_BEGINNING;

    static {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH_BEGINNING = calendar.getTimeInMillis();
    }

    public static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";

    private Constants() {
    } // never

    public static final String IN_BLOCK_ID = "inblockID";
    public static final String IN_BLOCK_HEIGHT = "inblockHeight";
    public static final String IN_TRANSACT_ID = "inTransactId";
    public static final String RANDOM = "random";

    public static final int GlubinaPoiska = 30000;

    public static final int ADVANCED_MESSAGING_VALIDATION = 450;   


    public static final int LAST_ALIASES_BLOCK = 1000; 

    public static final int CONTROL_TRX_TO_ORDINARY = 550; 
    public static final int FEE_MAX_10 = 1440; 

    public static final int CURRENT_BLOCK_VERSION = 3;
        
    public static final String GENESIS_SECRET_PHRASE ="Twas brillig, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And the mome raths outgrabe. The END";
     // SOLDO-5H5H-PRS4-UYTA-8GY83

   public static final int BEGIN_ZEROBLOCK_FIX = 0; 
 

    public static long getINITIAL_BASE_TARGET(int height) {
        if(height<1)return INITIAL_BASE_TARGET;

        long IBT = BigInteger.valueOf(2).pow(63).divide(BigInteger.valueOf(BLOCK_TIME * (Account.getAccount(Genesis.CREATOR_ID).getBalanceNQT()*(-1)/ONE_SLD) )).longValue();
        return IBT;
    }

}
