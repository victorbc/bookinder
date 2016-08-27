package equipe.projetoes.utilis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import equipe.projetoes.models.Filtros;
import equipe.projetoes.models.Livro;

/**
 * Created by Victor on 5/8/2016.
 */
public class HttpHandler {
    private Context ctx;
    private JSONArray livrosJson;
    private NodeList livrosXml;
    private List<Livro> livros;
    private Bitmap lastDraw;
    private int lastPosImageSet = 0;
    private boolean isReady = false;
    private int coverIndex;
    private int coverQt;
    private int coverQtPass;
    private int lastCoverNum = 0;
    private int lastBookNum = 0;

    public HttpHandler(Context ctx) {
        this.ctx = ctx;

        livros = new ArrayList<Livro>();
        getBooks(20);

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpJsonAsyncTask extends AsyncTask<String, Void, String> {
        private int index;

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(ctx, "Received!", Toast.LENGTH_LONG).show();
            //System.out.println(result);
            //etResponse.setText(result);


            JSONObject json = null; // convert String to JSONObject
            try {
                json = new JSONObject(result);

                livrosJson = json.getJSONArray("items"); // get articles array
                //livros.length(); // --> 2
                //livros.getJSONObject(0); // get first article in the array
                //livros.getJSONObject(0).names(); // get first article keys [title,url,categories,tags]
                //livros.getJSONObject(0).getString("url"); // return an article url


                index = 0;
                extractBooks();
                if (lastCoverNum == 0)
                    getCovers(0, 10);

                //System.out.println(livros.get(0).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }/* catch (IOException e) {
                e.printStackTrace();
            }*/

        }

        private void extractBooks() {
            try {
                JSONObject volume;
                Livro livro;
                for (int i = index; i < livrosJson.length(); i++) {
                    //System.out.println("for index " + i);

                    volume = (JSONObject) livrosJson.getJSONObject(i).get("volumeInfo");

                    livro = new Livro(((JSONObject) volume.get("imageLinks")).get("thumbnail").toString(),
                            volume.get("title").toString(),
                            ((JSONArray) volume.get("authors")).get(0).toString(),
                            volume.get("publisher").toString(),
                            volume.getInt("pageCount"),
                            0,
                            false,
                            false,
                            ((JSONObject) ((JSONArray) volume.get("industryIdentifiers")).get(0)).get("identifier").toString());
                    if (!livros.contains(livro))
                        livros.add(livro);

                    //  String drawable = ((JSONObject) volume.get("imageLinks")).get("thumbnail").toString();
                    // System.out.println(drawable);


                    //  new DrawableFromUrl().execute(drawable);
                    index++;
                }
            } catch (JSONException e) {
                index++;
                extractBooks();
            }
        }
    }


    private class HttpXmlAsyncTask extends AsyncTask<String, Void, String> {
        private int index;

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(ctx, "Received!", Toast.LENGTH_LONG).show();
            //System.out.println(result);
            //etResponse.setText(result);


            try {

                DocumentBuilderFactory dbf =
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder db = null;
                db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(result));
                Document doc = db.parse(is);
                livrosXml = doc.getElementsByTagName("work");


                index = 0;
                extractXmlBooks();
                if (lastCoverNum == 0)
                    getCovers(0, 10);


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void extractJsonBooks() {
            try {
                JSONObject volume;
                Livro livro;
                for (int i = index; i < livrosJson.length(); i++) {
                    //System.out.println("for index " + i);

                    volume = (JSONObject) livrosJson.getJSONObject(i).get("volumeInfo");

                    livro = new Livro(((JSONObject) volume.get("imageLinks")).get("thumbnail").toString(),
                            volume.get("title").toString(),
                            ((JSONArray) volume.get("authors")).get(0).toString(),
                            volume.get("publisher").toString(),
                            volume.getInt("pageCount"),
                            0,
                            false,
                            false,
                            ((JSONObject) ((JSONArray) volume.get("industryIdentifiers")).get(0)).get("identifier").toString());
                    if (!livros.contains(livro))
                        livros.add(livro);

                    //  String drawable = ((JSONObject) volume.get("imageLinks")).get("thumbnail").toString();
                    // System.out.println(drawable);


                    //  new DrawableFromUrl().execute(drawable);
                    index++;
                }
            } catch (JSONException e) {
                index++;
                extractJsonBooks();
            }
        }


        private void extractXmlBooks() {

            String authorTxt;
            String titleTxt;
            String imgTxt;
            String pubTxt;
            String pgTxt;
            try {
                for (int i = 0; i < livrosXml.getLength(); i++) {
                    Element element = (Element) livrosXml.item(i);

                    NodeList name = element.getElementsByTagName("name");
                    Element line = (Element) name.item(0);
                    authorTxt = getCharacterDataFromElement(line);
                    System.out.println("Name: " + authorTxt);

                    NodeList title = element.getElementsByTagName("title");
                    line = (Element) title.item(0);
                    titleTxt = getCharacterDataFromElement(line);
                    System.out.println("Title: " + titleTxt);

                    NodeList img = element.getElementsByTagName("image_url");
                    line = (Element) img.item(0);
                    imgTxt = getCharacterDataFromElement(line);

//                NodeList pub = element.getElementsByTagName("title");
//                line = (Element) title.item(0);
//                pubTxt = getCharacterDataFromElement(line);
//                System.out.println("Title: " + pubTxt);

//                NodeList pg = element.getElementsByTagName("title");
//                line = (Element) title.item(0);
//                pgTxt = getCharacterDataFromElement(line);
//                System.out.println("Title: " + pgTxt);


                    Livro livro;
                    livro = new Livro(imgTxt, titleTxt, authorTxt, "", 0, 0,
                            false,
                            false,
                            "");
                    if (!livros.contains(livro))
                        livros.add(livro);

                    //  String drawable = ((JSONObject) volume.get("imageLinks")).get("thumbnail").toString();
                    // System.out.println(drawable);


                    //  new DrawableFromUrl().execute(drawable);
                    index++;

                }
            } catch (Exception e) {
                index++;
                extractXmlBooks();
            }

        }


    }

    public void getCovers(int init, int qt) {
        System.out.println("getCovers(" + init + "," + qt + ")");
        // coverQtPass = 0;
        // coverQt = qt;
        // coverIndex = init;

        if (lastCoverNum < livros.size()) {
            for (int i = init; i < init + qt; i++) {
                if (lastCoverNum > i) continue;
                lastCoverNum++;
                try {
                    new DrawableFromUrl().execute(livros.get(i).getUrlImg());
                } catch (IndexOutOfBoundsException e) {
                    break;

                }

                //System.out.println("for");
            }


            saveImgs((ArrayList<Livro>) livros);
        }
    }

    public void getBooks(int qt) {
        System.out.println("getBooks(" + qt + ")");
        //  new HttpJsonAsyncTask().execute("https://www.googleapis.com/books/v1/users/109518442467553217123/bookshelves/1001/volumes?startIndex=" + livros.size() + "&maxResults=" + qt);
        new HttpXmlAsyncTask().execute("https://www.goodreads.com/search/index.xml?q=paulo+coelho&page=1&key=HEMYOGXpqJwvwnwG2AlLuQ&search[field]=author");
    }

    public void getBooks(int qt, Filtros filtro, String search_input) {
        new HttpXmlAsyncTask().execute("https://www.goodreads.com/search/index.xml?q=" + search_input.replace(" ", "+") + "&page=1&key=HEMYOGXpqJwvwnwG2AlLuQ&search[field]=" + filtro.getName());
    }


    private class DrawableFromUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                lastDraw = drawable_from_url(urls[0]);
                //coverQtPass++;
            } catch (IOException e) {
                e.printStackTrace();
                //getCovers(coverIndex++,coverQt-coverQtPass);
                return "error";
            }
            return "ok";
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            livros.get(lastPosImageSet).setDrawable(lastDraw);
            lastPosImageSet++;
            isReady = true;
        }

    }

    private void saveImgs(ArrayList<Livro> livros) {
        for (int i = 0; i < livros.size(); i++) {
            try {

                //create a file to write bitmap data
                //File f = new File(ctx.getCacheDir(), livros.get(i).getNome());
                //f.createNewFile();
                File f = getOutputMediaFile(livros.get(i).getNome());
                if (f.exists()) {
                    continue;
                }

//Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                livros.get(i).getDrawable().compress(Bitmap.CompressFormat.JPEG, 70, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                livros.get(i).setImgFilePath(f.getPath());

            } catch (Exception e) {
                Log.d("HTTPHandler", e.getMessage());

            }
        }
    }


    private File getOutputMediaFile(String name) {
        File mediaStorageDir = new File(ctx.getFilesDir(), "Bookinder");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("HTTPHandler", "failed to create directory");
                return null;
            }
        }
        // Create a media file name

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + name + ".jpg");

        return mediaFile;
    }


    public Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return x;
    }


    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }


    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public boolean isReady() {
        return isReady;
    }
}
