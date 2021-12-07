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

import soldo.util.Convert;

public final class Genesis {

    public static final long GENESIS_BLOCK_ID = -1925503574482420123L;
    public static final long CREATOR_ID = 6961384895484640367L;
    public static final long GENESIS_BLOCK_AMOUNT =Constants.MAX_BALANCE_centesimo; 
    public static final byte[] CREATOR_PUBLIC_KEY = {
        -30, -21, -18, 24, 53, 118, -128, -13, -68, -91, -27, 91, 36, -110, 11, -82, -54, 102, 15, 39, 22, 49, -87, -22, 91, 77, 99, 42, -117, -124, -12, 119
    };
    
    public static final long[] GENESIS_RECIPIENTS = {
            Long.parseUnsignedLong("634424746364288347")
    };
    
     
    public static final long[] first_RECIPIENTS = {
            Account.getId(Convert.parseHexString("08fa86ba6a005a82688addda201e80c958dee8cfd2531711988b56eaddebf335")),
            Account.getId(Convert.parseHexString("8a1c37c07f81b4587e8f53e2a150f13071bad67935eedf7433dfd0fc9339fc3d")),
            Account.getId(Convert.parseHexString("2846c0ea72f8fc006398eda337d366eb9971bc5e1df6b1c2a11dfd9275895273")),
            Account.getId(Convert.parseHexString("a0e125a88d419541f893a894687cf6d77117a186ea9d581c76d4c4e6fcb84713")),
            Account.getId(Convert.parseHexString("d2b367d581a0a5ef559f5821d071113bc31a9b913e53f94b6b8b6af885caa20d"))
               
    };

    public static final long[] GENESIS_AMOUNTS = {
            15000000000L
    };
    
    public static final byte[][] GENESIS_SIGNATURES = {
            {-63, 111, 126, 88, -29, -64, 31, 72, -20, 91, -116, 60, -108, 63, 44, 45, -27, -91, -109, 73, 117, 11, -123, -58, -25, -38, 35, -15, -106, -80, 39, 8, -114, -63, -118, 39, -52, -123, -70, -44, 8, 28, 63, 98, -65, 45, -92, -36, 125, -24, 94, 102, 119, 98, 124, -105, 91, -78, 121, 36, -119, -104, -72, 42}

        };

    public static final byte[] GENESIS_BLOCK_SIGNATURE = new byte[]{
    	25, 123, -82, 7, 126, 46, -89, -49, -72, 54, 0, 64, -95, -99, 109, -42, -101, 19, -8, 93, -37, -87, -27, -50, -49, 44, 113, 60, 123, -73, -96, 3, -116, 117, -49, 85, -70, 2, -100, -33, -89, 2, 52, -37, 10, 33, 47, -28, -59, 50, -7, 48, -51, 71, 48, -47, 43, 47, 99, -72, -120, -40, -18, 66
    };

    private Genesis() {} // never

}
