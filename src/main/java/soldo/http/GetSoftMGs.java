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

import soldo.Soldo;
import soldo.SLDException;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;
import soldo.SoftMGs;
public final class GetSoftMGs extends APIServlet.APIRequestHandler {

    static final GetSoftMGs instance = new GetSoftMGs();

    private GetSoftMGs() {
        super(new APITag[] {APITag.SOFTMG}, "account");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws SLDException {
        long accountId = ParameterParser.getAccountId(req, true);
        
        SoftMGs metrics = Soldo.softMG().getMetrics(accountId);
        return JSONData.accountSLDmg(metrics,accountId);
    }

}