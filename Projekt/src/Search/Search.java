package Search;

public class Search extends AppCompatActivity{

    public static String[] Gym(String args) {
        //String input = new String (Search.Input());
        //String[] Compare = new String[] {(GetGyms.GymID())};
        //ArrayList<String> list = Compare;
        //String[] gyms = CompareResult;
        return null;
    }

    private String Input() {
        SearchView login_Search = (SearchView) findViewById(R.id.login_Search);
        String id = login_Search.getQuery().toString();
        return id;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}