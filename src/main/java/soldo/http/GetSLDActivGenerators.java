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

import soldo.SLDException;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;
import soldo.SimplDimpl;
public final class GetSLDActivGenerators extends APIServlet.APIRequestHandler {

    static final GetSLDActivGenerators instance = new GetSLDActivGenerators();

    private GetSLDActivGenerators() {
        super(new APITag[] {APITag.SLDActivGenerators});
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws SLDException {
   
        return SimplDimpl.getSLDgenerators();
    }

}
