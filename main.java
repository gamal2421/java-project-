 class Library {
    private int libraryId;
    private String libraryName;
    private List<Customer> members;
    private List<Transaction> transactions;
    private int managerId;
    private String address;


    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Customer> members) {
        this.members = members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Customer> transactions) {
        this.transactions = transactions;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}



