package soldo.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import soldo.Account;
import soldo.Soldo;
import soldo.SOLException;
import soldo.Transaction;
import soldo.db.DbIterator;
import soldo.util.Convert;
import soldo.util.SoldoTree;

import javax.servlet.http.HttpServletRequest;

public class GetMostLoyalParent extends SoldoTree.APIHierarchyRequestHandler {

    static final GetMostLoyalParent instance = new GetMostLoyalParent();

    private GetMostLoyalParent() {
        super(new APITag[] {APITag.SOFTMG}, "accountChild");
    }

    @Override
    protected JSONStreamAware processHierarchyRequest(HttpServletRequest req) throws SOLException {

        long accountId = ParameterParser.getAccountId(req, "accountChild", true);
        if (accountId == 0L)
            return SoldoTree.createErrorResponse("Invalid \"accountChild\"!", 9899);
        Account account = Account.getAccount(accountId);
        if (account == null)
            throw new SOLException.NotValidException("Invalid account");
        SoldoTree.AccountLoyaltyContainer container = SoldoTree.getMostLoyalParentFaster (account);
        if (container == null || container.account == null)
            throw new SOLException.NotValidException ("Loyal parent not found (2)");
        JSONObject response = new JSONObject();
        response.put("loyalParent", Convert.rsAccount(container.account.getId()));
        response.put("loyalty", container.loyalty);
        return response;
    }
}
