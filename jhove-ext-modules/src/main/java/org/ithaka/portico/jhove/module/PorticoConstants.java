package org.ithaka.portico.jhove.module;

import edu.harvard.hul.ois.jhove.Agent;
import edu.harvard.hul.ois.jhove.Agent.Builder;
import edu.harvard.hul.ois.jhove.AgentType;

public class PorticoConstants {

    // Module agent information
    public static final String PORTICOVENDORNAME = "Portico";
    public static final AgentType PORTICOAGENTTYPE = AgentType.EDUCATIONAL;
    public static final String PORTICOAGENTADDRESS = "Portico, "
            + "100 Campus Drive, Suite 100, "
            + "Princeton, NJ 08540";
    public static final String PORTICOAGENTTELEPHONE = "+1 (609) 986-2222";
    public static final String PORTICOAGENTEMAIL = "support@portico.org";
    public static final String RIGHTS_STR1 = "Copyright ";
    public static final String RIGHTS_STR2 = " by Portico. Released under the GNU Lesser General Public License.";

    private PorticoConstants() {
        // hide the constructor
    }

    /**
     * Constructs rights statement using (String) year value from each PTC module
     *
     * @param strYear Copyright year for each module, as String
     * @return rights statement
     */
    public static String porticoRightsStmt(String strYear) {
        StringBuffer sb = new StringBuffer(RIGHTS_STR1);
        sb.append(strYear);
        sb.append(RIGHTS_STR2);
        return sb.toString();
    }

    public static Agent porticoAgent() {
        return new Builder(PORTICOVENDORNAME, PORTICOAGENTTYPE)
                .address(PORTICOAGENTADDRESS)
                .telephone(PORTICOAGENTTELEPHONE)
                .email(PORTICOAGENTEMAIL).build();
    }

}
