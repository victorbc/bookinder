package equipe.projetoes.data;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.models.Account;
import equipe.projetoes.models.Livro;
import equipe.projetoes.models.LivroUser;
import equipe.projetoes.models.Match;
import equipe.projetoes.util.Callback;
import equipe.projetoes.util.Constants;
import equipe.projetoes.util.HttpHandler;

/**
 * Created by stenio on 9/18/16.
 */
public class RestDAO implements RestDAOInterface {
    private String token;
    private String host;
    static private RestDAO instancia;

    private RestDAO() {
        setHost(Constants.DEFAULT_HOST);
    }

    /**
     * @param host http[s]://hostname:port
     */
    public void setHost(String host) {

        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    static public RestDAO getInstance() {
        if (instancia == null) {
            instancia = new RestDAO();
        }
        return instancia;
    }

    private void getUserToken(String username, String password, final Callback<String> callback) {
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
                            callback.execute(result.get("token").toString());
                        } catch (Exception e) {
                            callback.execute("");
                        }
                    }
                });

        postTask.execute();
    }

    @Override
    public void authenticate(String username, String password, final Callback<Account> callback) {
        getUserToken(username, password, new Callback<String>() {
            @Override
            public void execute(String result) {
                token = result;
                getUserProfile(callback);
            }
        });
    }

    @Override
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
                                    } catch (Exception e) {
                                    }
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

        try {
            acc.setEmail(json.getString("email"));
            acc.setFirstTime(json.getBoolean("first_time"));
            acc.setEmail_facebook(json.getString("email_facebook"));
            acc.setEmail_google(json.getString("email_google"));
            acc.setId(json.getLong("id"));
            acc.setLogin(json.getString("username"));

            return acc;
        } catch (Exception e) {
            return null;
        }
    }

    private Livro createLivroFromJson(JSONObject json) {
        Livro livro = new Livro();

        try {
            livro.setAutor(json.getString("autor"));
            livro.setEditora(json.getString("editora"));
            livro.setISBN(json.getString("isbn"));
            livro.setImgFilePath(json.getString("img_file_path"));
            livro.setNome(json.getString("nome"));
            livro.setPg(json.getInt("paginas"));
            livro.setResId(json.getInt("res_id"));
            livro.setUrlImg(json.getString("url_img"));
        } catch (Exception e) {
        }

        return livro;
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

    private JSONObject createJsonFromLivro(Livro livro) {
        JSONObject json = new JSONObject();

        try {
            json.put("isbn", livro.getISBN());
            json.put("nome", livro.getNome());
            json.put("autor", livro.getAutor());
            json.put("editora", livro.getEditora());
            json.put("paginas", livro.getPg());
            json.put("img_file_path", livro.getImgFilePath());
            json.put("url_img", livro.getUrlImg());
            json.put("res_id", livro.getResId());
        } catch (Exception e) {
        }

        return json;
    }

    private JSONObject createJsonFromLivroUser(LivroUser livro) {
        JSONObject json = new JSONObject();

        try {
            json.put("id", livro.getId());
            json.put("book", this.host + "/books/" + livro.getLivro().getISBN() + "/");
            json.put("favorite", livro.isFavorite());
            json.put("tradeable", livro.isTradeable());
            json.put("blocked", livro.isBlocked());
            json.put("owned", livro.isOwned());
            json.put("liked", livro.isLiked());
            json.put("interested", livro.isInterested());
            json.put("pages_read", livro.getReadPages());
        } catch (Exception e) {
        }

        return json;
    }

    private Pair<String, String> tokenHeader() {
        return tokenHeader(this.token);
    }

    private Pair<String, String> tokenHeader(String userToken) {
        return new Pair<>("Authorization", "Token " + userToken);
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
                        getUserToken(acc.getLogin(), acc.getPass(), new Callback<String>() {
                            @Override
                            public void execute(String resultToken) {
                                createProfile(acc, resultToken, new Callback<Account>() {
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

    private void createProfile(final Account account, String accToken, final Callback<Account> callback) {
        JSONObject body = createJsonFromAccount(account);

        HttpPostTask postTask = new HttpPostTask(
                this.host + "/users_profiles/",
                body,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            result.put("username", account.getLogin());
                            result.put("email", account.getEmail());

                            Account acc = createAccountFromJson(result);
                            callback.execute(acc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                tokenHeader(accToken));

        postTask.execute();
    }

    @Override
    public void createLivro(final Livro livro, final Callback<Livro> callback) {
        JSONObject body = createJsonFromLivro(livro);

        HttpPostTask httpPost = new HttpPostTask(
                this.host + "/books/",
                body,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            result.getString("nome"); // Se for null vai gerar excecao
                            callback.execute(createLivroFromJson(result));
                        } catch (JSONException e) {
                            getLivro(livro.getISBN(), new Callback<Livro>() {
                                @Override
                                public void execute(Livro result) {
                                    callback.execute(result);
                                }
                            });
                        }
                    }
                },
                tokenHeader()
        );

        httpPost.execute();
    }

    @Override
    public void getLivro(String livroUrlOrISBN, final Callback<Livro> callback) {
        String url = this.host + "/books/" + livroUrlOrISBN;

        if (livroUrlOrISBN.startsWith("http"))
            url = livroUrlOrISBN;

        HttpGetTask httpGet = new HttpGetTask(
                url,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        Livro resultLivro = createLivroFromJson(result);
                        callback.execute(resultLivro);
                    }
                },
                tokenHeader()
        );

        httpGet.execute();
    }

    @Override
    public void getLivroUser(int id, final Callback<LivroUser> callback) {
        HttpGetTask httpGet = new HttpGetTask(
                this.host + "/library/" + id,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            final int id = result.getInt("id");
                            final Boolean favorite = result.getBoolean("favorite");
                            final Boolean tradeable = result.getBoolean("tradeable");
                            final Boolean blocked = result.getBoolean("blocked");
                            final Boolean owned = result.getBoolean("owned");
                            final Boolean liked = result.getBoolean("liked");
                            final Boolean interested = result.getBoolean("interested");
                            final Integer readPages = result.getInt("read_pages");

                            getLivro(result.getString("book"), new Callback<Livro>() {
                                @Override
                                public void execute(Livro result) {
                                    LivroUser livroUser = new LivroUser(result);
                                    livroUser.setId(id);
                                    livroUser.setFavorite(favorite);
                                    livroUser.setTradeable(tradeable);
                                    livroUser.setBlocked(blocked);
                                    livroUser.setOwned(owned);
                                    livroUser.setLiked(liked);
                                    livroUser.setInterested(interested);
                                    livroUser.setReadPages(readPages);

                                    callback.execute(livroUser);
                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                },
                tokenHeader()
        );

        httpGet.execute();
    }

    @Override
    public void getLibrary(final Callback<List<LivroUser>> callback) {
        HttpGetTask httpGet = new HttpGetTask(
                this.host + "/library?blocked=false",
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            final List<LivroUser> livros = new ArrayList<>();

                            JSONArray results = result.getJSONArray("results");

                            final int resultsLength = results.length();
                            for (int i = 0; i < resultsLength; i++) {
                                JSONObject jsonLivroUser = results.getJSONObject(i);
                                final int finalI = i;
                                getLivroUser(jsonLivroUser.getInt("id"), new Callback<LivroUser>() {
                                    @Override
                                    public void execute(LivroUser result) {
                                        livros.add(result);
                                        if (livros.size() == resultsLength)
                                            callback.execute(livros);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            callback.execute(null);
                        }
                    }
                },
                tokenHeader()
        );

        httpGet.execute();
    }

    @Override
    public void addBookToLibrary(Livro livro, final Callback<LivroUser> callback) {
        createLivro(livro, new Callback<Livro>() {
            @Override
            public void execute(Livro result) {
                adicionarLivroBibliotecaApenas(result, new Callback<LivroUser>() {
                    @Override
                    public void execute(LivroUser result) {
                        callback.execute(result);
                    }
                });
            }
        });
    }

    private void adicionarLivroBibliotecaApenas(final Livro livro, final Callback<LivroUser> callback) {
        if (isAuthenticated()) {
            String isbn = livro.getISBN();
            JSONObject body = new JSONObject();
            try {
                body.put("book", this.host + "/books/" + isbn + "/");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpPostTask httpPost = new HttpPostTask(
                    this.host + "/library/",
                    body,
                    new Callback<JSONObject>() {
                        @Override
                        public void execute(final JSONObject resultJson) {
                            try {
                                getLivro(resultJson.getString("book"), new Callback<Livro>() {
                                    @Override
                                    public void execute(Livro resultLivro) {
                                        try {
                                            LivroUser livroUser = new LivroUser(resultLivro);
                                            livroUser.setId(resultJson.getInt("id"));

                                            callback.execute(livroUser);
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    tokenHeader()
            );

            httpPost.execute();
        } else {
            authenticate("stenio", "admin123", new Callback<Account>() {
                @Override
                public void execute(Account result) {
                    if (result != null) {
                        addBookToLibrary(livro, callback);
                    }
                }
            });
        }
    }

    @Override
    public void update(LivroUser livro, final Callback<LivroUser> callback) {
        JSONObject body = createJsonFromLivroUser(livro);

        HttpPutTask httpPut = new HttpPutTask(
                this.host + "/library/" + livro.getId() + "/",
                body,
                new Callback<JSONObject>() {
                    @Override
                    public void execute(JSONObject result) {
                        try {
                            getLivroUser(result.getInt("id"), new Callback<LivroUser>() {
                                @Override
                                public void execute(LivroUser result) {
                                    callback.execute(result);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                tokenHeader()
        );

        httpPut.execute();
    }

    @Override
    public void getMatchList(Callback<List<Match>> callback) {

    }

    @Override
    public void getPristineMatchList(final Callback<List<Match>> callback) {
        if (isAuthenticated()) {

        } else {
            authenticate("stenio", "admin123", new Callback<Account>() {
                @Override
                public void execute(Account result) {
                    if (result != null)
                        getPristineMatchList(callback);
                }
            });
        }
    }

    @Override
    public void rejectMatch(final Match match, final Callback<Integer> callback) {
        if (isAuthenticated()) {

        } else {
            authenticate("stenio", "admin123", new Callback<Account>() {
                @Override
                public void execute(Account result) {
                    if (result != null)
                        rejectMatch(match, callback);

                }
            });
        }

    }

    @Override
    public void acceptMatch(final Match match, final Callback<Integer> callback) {
        if (isAuthenticated()) {

        } else {
            authenticate("stenio", "admin123", new Callback<Account>() {
                @Override
                public void execute(Account result) {
                    if (result != null)
                        acceptMatch(match, callback);
                }
            });
        }
    }

    @Override
    public void logOff() {
        token = null;
    }

    @Override
    public boolean isAuthenticated() {
        return (token != null);
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
            try {
                JSONObject json = new JSONObject(result);
                ; // convert String to JSONObject
                json.put("statusCode", this.getStatus());

                callback.execute(json);
            } catch (JSONException e) {
            }

        }
    }

    private class HttpPutTask extends AsyncTask<String, Void, String> {
        private String url;
        private JSONObject body;
        private Callback<JSONObject> callback;
        private Pair<String, String> headers[];

        public HttpPutTask(String url, JSONObject body, Callback<JSONObject> callback,
                           Pair<String, String>... headers) {
            this.url = url;
            this.body = body;
            this.callback = callback;
            this.headers = headers;
        }

        @Override
        protected String doInBackground(String... urls) {
            return HttpHandler.PUT(this.url, this.body, this.headers);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                ; // convert String to JSONObject
                json.put("statusCode", this.getStatus());

                callback.execute(json);
            } catch (JSONException e) {
            }

        }
    }
}