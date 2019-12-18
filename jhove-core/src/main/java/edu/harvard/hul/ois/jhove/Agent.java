/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment Copyright 2003 by JSTOR
 * and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove;

/**
 * Encapsulates information about agents, either individual persons or corporate
 * bodies.
 */
public final class Agent {
    private static final Agent HARVARD = new Builder(
            "Harvard University Library", AgentType.EDUCATIONAL)
            .address(
                    "Office for Information Systems, " + "90 Mt. Auburn St., "
                            + "Cambridge, MA 02138")
            .telephone("+1 (617) 495-3724")
            .email("jhove-support@hulmail.harvard.edu").build();

    private static final Agent OPF = new Builder(
            "Open Preservation Foundation", AgentType.NONPROFIT)
            .web("http://openpreservation.org")
            .email("jhove@openpreservation.org").build();

    private static final Agent BNF = new Builder(
            "Bibliothèque nationale de France", AgentType.EDUCATIONAL)
            .web("http://www.bnf.fr").build();

    /******************************************************************
     * PRIVATE INSTANCE FIELDS.
     ******************************************************************/

    private final String _name;
    private final AgentType _type;
    private final String _address;
    private final String _telephone;
    private final String _fax;
    private final String _email;
    private final String _web;
    private final String _note;

    /******************************************************************
     * CLASS CONSTRUCTOR.
     ******************************************************************/

    /**
     * Creates an Agent given a name and an AgentType.
     */
    private Agent(final String name, final AgentType type,
            final String address, final String telephone, final String fax,
            final String email, final String web, final String note) {
        _name = name;
        _type = type;
        this._address = address;
        this._telephone = telephone;
        this._fax = fax;
        this._email = email;
        this._web = web;
        this._note = note;
    }

    /******************************************************************
     * PUBLIC INSTANCE METHODS.
     ******************************************************************/

    /**
     * Returns the value of the address property.
     */
    public String getAddress() {
        return _address;
    }

    /**
     * Returns the value of the email property.
     */
    public String getEmail() {
        return _email;
    }

    /**
     * Returns the value of the fax property.
     */
    public String getFax() {
        return _fax;
    }

    /**
     * Returns the value of the name property.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the value of the note property.
     */
    public String getNote() {
        return _note;
    }

    /**
     * Returns the value of the telephone property.
     */
    public String getTelephone() {
        return _telephone;
    }

    /**
     * Returns the value of the type property.
     */
    public AgentType getType() {
        return _type;
    }

    /**
     * Returns the value of the web property.
     */
    public String getWeb() {
        return _web;
    }

    public static final Agent harvardInstance() {
        return HARVARD;
    }

    public static final Agent opfInstance() {
        return OPF;
    }

    public static final Agent bnfInstance() {
        return BNF;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_address == null) ? 0 : _address.hashCode());
        result = prime * result + ((_email == null) ? 0 : _email.hashCode());
        result = prime * result + ((_fax == null) ? 0 : _fax.hashCode());
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_note == null) ? 0 : _note.hashCode());
        result = prime * result
                + ((_telephone == null) ? 0 : _telephone.hashCode());
        result = prime * result + ((_type == null) ? 0 : _type.hashCode());
        result = prime * result + ((_web == null) ? 0 : _web.hashCode());
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Agent other = (Agent) obj;
        if (_address == null) {
            if (other._address != null)
                return false;
        } else if (!_address.equals(other._address))
            return false;
        if (_email == null) {
            if (other._email != null)
                return false;
        } else if (!_email.equals(other._email))
            return false;
        if (_fax == null) {
            if (other._fax != null)
                return false;
        } else if (!_fax.equals(other._fax))
            return false;
        if (_name == null) {
            if (other._name != null)
                return false;
        } else if (!_name.equals(other._name))
            return false;
        if (_note == null) {
            if (other._note != null)
                return false;
        } else if (!_note.equals(other._note))
            return false;
        if (_telephone == null) {
            if (other._telephone != null)
                return false;
        } else if (!_telephone.equals(other._telephone))
            return false;
        if (_type == null) {
            if (other._type != null)
                return false;
        } else if (!_type.equals(other._type))
            return false;
        if (_web == null) {
            if (other._web != null)
                return false;
        } else if (!_web.equals(other._web))
            return false;
        return true;
    }

    public static final Agent newW3CInstance() {
        Builder builder = new Builder("Word Wide Web Consortium",
                AgentType.NONPROFIT)
                .address(
                        "Massachusetts Institute of Technology, "
                                + "Computer Science and Artificial Intelligence Laboratory, "
                                + "32 Vassar Street, Room 32-G515, "
                                + "Cambridge, MA 02139")
                .telephone("(617) 253-2613").fax("(617) 258-5999")
                .web("http://www.w3.org/");
        return builder.build();
    }

    public static final Agent newIsoInstance() {
        Builder builder = new Builder("ISO", AgentType.STANDARD)
                .address(
                        "1, rue de Varembe, Casa postale 56, "
                                + "CH-1211, Geneva 20, Switzerland")
                .telephone("+41 22 749 01 11").fax("+41 22 733 34 30")
                .email("iso@iso.ch").web("http://www.iso.org/");
        return builder.build();
    }

    public static final Agent newAdobeInstance() {
        Builder builder = new Builder("Adobe Systems, Inc.",
                AgentType.COMMERCIAL)
                .address("345 Park Avenue, San Jose, California 95110-2704")
                .telephone("+1 (408) 536-6000").fax("+1 (408) 537-6000")
                .web("http://www.adobe.com/");
        return builder.build();
    }

    @SuppressWarnings("hiding")
    public static class Builder {
        private String name;
        private AgentType type = AgentType.EDUCATIONAL;
        private String address;
        private String telephone;
        private String fax;
        private String email;
        private String web;
        private String note;

        public Builder(final String name, final AgentType type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Sets the value of the address property.
         */
        public Builder address(final String address) {
            this.address = address;
            return this;
        }

        /**
         * Sets the value of the email property.
         */
        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the value of the fax property.
         */
        public Builder fax(final String fax) {
            this.fax = fax;
            return this;
        }

        /**
         * Sets the value of the name property.
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the value of the note property.
         */
        public Builder note(final String note) {
            this.note = note;
            return this;
        }

        /**
         * Sets the value of the telephone property.
         */
        public Builder telephone(final String telephone) {
            this.telephone = telephone;
            return this;
        }

        /**
         * Sets the value of the web property.
         */
        public Builder web(final String web) {
            this.web = web;
            return this;
        }

        /**
         * @return the immutable Agent instance build from the builder contents
         */
        public Agent build() {
            return new Agent(this.name, this.type, this.address,
                    this.telephone, this.fax, this.email, this.web, this.note);
        }
    }
}
