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

package soldo.http;


import soldo.Account;
import soldo.Alias;
import soldo.Attachment;
import soldo.Constants;
import soldo.SLDException;
import soldo.util.Convert;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static soldo.http.JSONResponses.INCORRECT_ALIAS_LENGTH;
import static soldo.http.JSONResponses.INCORRECT_ALIAS_NAME;
import static soldo.http.JSONResponses.INCORRECT_URI_LENGTH;
import static soldo.http.JSONResponses.MISSING_ALIAS_NAME;

public final class SetAlias extends CreateTransaction {

    static final SetAlias instance = new SetAlias();

    private SetAlias() {
        super(new APITag[] {APITag.ALIASES, APITag.CREATE_TRANSACTION}, "aliasName", "aliasURI");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws SLDException {
        return JSONResponses.FEATURE_NOT_AVAILABLE;

    }

}
