package equipe.projetoes.utilis;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import equipe.projetoes.exceptions.BookinderException;
import equipe.projetoes.models.Account;
import equipe.projetoes.models.Livro;
import equipe.projetoes.models.LivroUser;

/**
 * Created by stenio on 9/18/16.
 */
public class RestDAO implements RestDAOInterface {
    private String token;
    private String userID;
    private String host;

    /**
     *
     * @param host http[s]://hostname:port
     */
    public RestDAO(String host) {
        this.host = host;
    }

    public void test() {
        HttpGetTask getTask = new HttpGetTask("http://rest-service.guides.spring.io/greeting",
                new Callback<JSONObject>() {

                    @Override
                    public void execute(JSONObject result) {
                        Log.wtf("TEST_____", result.toString());
                    }
                });

        getTask.execute();
    }

    private void getUserToken(String username, String password, final Callback<Account> callback) {
        JSONObject body = new JSONObject();

        try {
            body.put("username", username);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPostTask postTask = new HttpPostTask(
                this.host + "/api-token-auth/",
                body,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            token = result.get("token").toString();

                            Account acc = createAccountFromJson(result);
                            callback.execute(acc);
                        } catch (Exception e) {
                            callback.execute(null);
                        }
                    }
                });

        postTask.execute();
    }

    @Override
    public void authenticate(String username, String password, final Callback<Account> callback) {
        getUserToken(username, password, new Callback<Account>() {
            @Override
            public void execute(Account result) {
                getUserProfile(callback);
            }
        });
    }

    public void getUserProfile(final Callback<Account> callback) {
        HttpGetTask getTask = new HttpGetTask(
                this.host + "/users_profiles/",
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            JSONArray users = result.getJSONArray("results");
                            final JSONObject user = users.getJSONObject(0);

                            getUser(user.getString("user"), new Callback<JSONObject>() {
                                @Override
                                public void execute(JSONObject result) {
                                    try {
                                        user.put("username", result.getString("username"));
                                        user.put("first_name", result.getString("first_name"));
                                        user.put("last_name", result.getString("last_name"));
                                        user.put("email", result.getString("email"));

                                        Account userAcc = createAccountFromJson(user);

                                        callback.execute(userAcc);
                                    } catch (Exception e){}
                                }
                            });
                        } catch (Exception e) {
                            callback.execute(null);
                        }
                    }
                },
                tokenHeader());

        getTask.execute();
    }

    private void getUser(String user_url, final Callback<JSONObject> callback) {

        HttpGetTask getTask = new HttpGetTask(
                user_url,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        callback.execute(result);
                    }
                },
                tokenHeader());

        getTask.execute();
    }

    private Account createAccountFromJson(JSONObject json) {
        Account acc = new Account();

        try{
            acc.setEmail(json.getString("email"));
            acc.setFirstTime(json.getBoolean("first_time"));
            acc.setEmail_facebook(json.getString("email_facebook"));
            acc.setEmail_google(json.getString("email_google"));
            acc.setId(json.getLong("id"));
            acc.setLogin(json.getString("username"));

            return acc;
        }
        catch(Exception e) {
            return null;
        }
    }

    private JSONObject createJsonFromAccount(Account acc) {
        JSONObject json = new JSONObject();

        try {
            json.put("email", acc.getEmail());
            json.put("email_facebook", acc.getEmail_facebook());
            json.put("email_google", acc.getEmail_google());
            json.put("username", acc.getLogin());
            json.put("password", acc.getPass());
            json.put("first_time", acc.isFirstTime());
            json.put("first_name", "");
            json.put("last_name", "");

            return json;
        } catch (Exception e) {
            return null;
        }
    }

    private Pair<String, String> tokenHeader() {
        return new Pair<>("Authorization", "Token "+this.token);
    }

    @Override
    public void createUser(final Account acc, final Callback<Account> callback) {
        JSONObject body = createJsonFromAccount(acc);

        HttpPostTask postTask = new HttpPostTask(
                this.host + "/users/",
                body,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        getUserToken(acc.getLogin(), acc.getPass(), new Callback<Account>() {
                            @Override
                            public void execute(Account result) {
                                createProfile(result, new Callback<Account>() {
                                    @Override
                                    public void execute(Account result) {
                                        callback.execute(result);
                                    }
                                });
                            }
                        });
                    }
                });

        postTask.execute();
    }

    private void createProfile(final Account account, final Callback<Account> callback) {
        HttpPostTask postTask = new HttpPostTask(
                this.host + "/users_profiles/",
                new JSONObject(),
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            result.put("username", account.getLogin());
                            Account acc = createAccountFromJson(result);
                            callback.execute(acc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                tokenHeader());

        postTask.execute();
    }

    @Override
    public void createBook(Livro livro) throws BookinderException {

    }

    @Override
    public List<Livro> getLivrosWith(String text) {
        return null;
    }

    @Override
    public Livro getLivro(String isbn) {
        return null;
    }

    @Override
    public List<LivroUser> getLibrary() {
        return null;
    }

    @Override
    public LivroUser addBookToLibrary(Livro livro) throws BookinderException {
        return null;
    }

    @Override
    public void update(LivroUser livro) throws BookinderException {

    }

    private class HttpGetTask extends AsyncTask<String, Void, String> {
        private String url;
        private Callback<JSONObject> callback;
        private Pair<String, String> headers[];

        public HttpGetTask(String url, Callback<JSONObject> callback,
                           Pair<String, String>... headers) {
            this.url = url;
            this.callback = callback;
            this.headers = headers;
        }


        @Override
        protected String doInBackground(String... urls) {
            return HttpHandler.GET(this.url, this.headers);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                json.put("statusCode", this.getStatus());

                callback.execute(json);
            } catch (JSONException e) {
                callback.execute(null);
            }

        }
    }

    private class HttpPostTask extends AsyncTask<String, Void, String> {
        private String url;
        private JSONObject body;
        private Callback<JSONObject> callback;
        private Pair<String, String> headers[];

        public HttpPostTask(String url, JSONObject body, Callback<JSONObject> callback,
                            Pair<String, String>... headers) {
            this.url = url;
            this.body = body;
            this.callback = callback;
            this.headers = headers;
        }

        @Override
        protected String doInBackground(String... urls) {
            return HttpHandler.POST(this.url, this.body, this.headers);
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject json = null; // convert String to JSONObject
            try {
                json = new JSONObject(result);
                json.put("statusCode", this.getStatus());

                callback.execute(json);
            } catch (JSONException e) {
            }

        }
    }
}