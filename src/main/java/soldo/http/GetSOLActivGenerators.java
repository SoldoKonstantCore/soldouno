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

import soldo.SOLException;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;
import soldo.SimplDimpl;
public final class GetSOLActivGenerators extends APIServlet.APIRequestHandler {

    static final GetSOLActivGenerators instance = new GetSOLActivGenerators();

    private GetSOLActivGenerators() {
        super(new APITag[] {APITag.SOLActivGenerators});
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws SOLException {
   
        return SimplDimpl.getSOLgenerators();
    }

}
