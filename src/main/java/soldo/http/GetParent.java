package soldo.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import soldo.Account;
import soldo.Soldo;
import soldo.SOLException;
import soldo.util.Convert;
import soldo.util.SoldoTree;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetParent extends SoldoTree.APIHierarchyRequestHandler {

    static final GetParent instance = new GetParent();
    private GetParent () {
       
        super (new APITag[] {APITag.ACCOUNTS}, "account");
        
    }
    @Override
    protected JSONStreamAware processHierarchyRequest(HttpServletRequest request) throws SOLException {

        long account = ParameterParser.getAccountId(request, true);

        if (account == 0L || Account.getAccount(account) == null )
            return SoldoTree.createErrorResponse("Invalid \"account\"!", 9999);

        JSONObject response = new JSONObject();
      
        PreparedStatement statement;
        ResultSet rs;
        try {
            statement = Soldo.softMG().getConnection().prepareStatement("select parent_id from soft where id=?");
            statement.setLong(1, account);
            rs = statement.executeQuery();
            long solvedParent = 0l;
            while (rs.next()) {
                solvedParent = rs.getLong(1);
            }
            String rsParent = Convert.rsAccount(solvedParent);

            response.put("accountRS", Convert.rsAccount(account));
            response.put("parentRS", (solvedParent==0?"":rsParent) );
            response.put("parent", (solvedParent==0?"":Convert.parseAccountId(rsParent)) );
            
            rs.close();
            statement.close();
            
            return response;
        } catch (SQLException e) {
            throw new SOLException.NotValidException (e.getMessage(), e.getCause());
        }
    }
}
