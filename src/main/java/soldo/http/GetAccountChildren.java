package soldo.http;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import soldo.Account;
import soldo.Soldo;
import soldo.SOLException;
import soldo.util.Logger;
import soldo.util.SoldoTree;
import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import static soldo.util.SoldoTree.getDirectChildrenOf;
import static soldo.util.SoldoTree.getParentOf;
import static soldo.util.SoldoTree.getRootAccountMinimal;
import static soldo.util.SoldoTree.AccountMinimal;

public class GetAccountChildren extends SoldoTree.APIHierarchyRequestHandler {

    static final GetAccountChildren instance = new GetAccountChildren ();

    private GetAccountChildren() {
        super(new APITag[] {APITag.ACCOUNTS}, "account", "firstIndex");
    }

    public static final int         MAX_DEPTH_PER_REQUEST =                 10;   

    @Override
    protected JSONStreamAware processHierarchyRequest(HttpServletRequest req) throws SOLException {
        if (Soldo.softMG().getConnection() == null) {
            JSONObject response = new JSONObject();
            response.put("errorDescription", "GetAccountChildren API failed to connect to the database");
            response.put("errorCode", "123");
            return response;
        }

        final long accountID = ParameterParser.getAccountId(req, true);
        if (accountID == 0L) {
            return SoldoTree.createErrorResponse("Invalid account!", 9699);
        }

        final int startIndex = ParameterParser.getFirstIndex(req);

        final Account accountObject = Account.getAccount(accountID);

        if (accountObject == null)
            return SoldoTree.createErrorResponse("Account "+accountID+" not found", 9601);

        final AccountMinimal parent = getParentOf(accountID);
        final AccountMinimal account = getRootAccountMinimal(accountID);

        if (parent == null || account == null)
            return SoldoTree.createErrorResponse("Impossible to solve hierarchy for this account", 9698);

        List<AccountMinimal> children;

        try {
            children = getDirectChildrenOf(accountID, 1, 2, true, startIndex, true);
        } catch (SQLException e) {
            Logger.logErrorMessage(e.getMessage(), e);
            return SoldoTree.createErrorResponse("Failed to process request", 9699);
        }

        JSONObject response = new JSONObject();
        JSONArray childrenJson = new JSONArray();

        for (AccountMinimal a : children) {
            childrenJson.add(a.toJSONObject());
        }

        response.put("children", childrenJson);

        return response;
    }

}

