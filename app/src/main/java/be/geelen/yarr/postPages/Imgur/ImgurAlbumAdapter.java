package be.geelen.yarr.postPages.Imgur;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class ImgurAlbumAdapter extends FragmentStatePagerAdapter {
    private String json;
    private JSONObject rootObject;
    private JSONObject albumObject;
    private JSONArray imagesArray;

    public ImgurAlbumAdapter(FragmentManager fm, String url) {
        super(fm);

        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";
                        //"{\"album\":{\"title\":\"A drummer's storage solution\",\"description\":null,\"cover\":\"bWeOT\",\"layout\":\"blog\",\"images\":[{\"image\":{\"title\":\"Rails + posts cut and stained.\",\"caption\":\"4x4 cedar posts for the corners.  2x8 top rails, 2x6 bottoms.\",\"hash\":\"bWeOT\",\"datetime\":\"2012-12-01 20:50:42\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":1552,\"height\":2592,\"size\":338861,\"views\":121434,\"bandwidth\":41149246674},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/bWeOT.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/bWeOT\",\"small_square\":\"http:\\/\\/i.imgur.com\\/bWeOTs.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/bWeOTl.jpg\"}},{\"image\":{\"title\":\"Finished with a satin clearcoat.\",\"caption\":\"\",\"hash\":\"Kq7GP\",\"datetime\":\"2012-12-01 20:51:00\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2592,\"height\":1552,\"size\":312851,\"views\":112516,\"bandwidth\":35200743116},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/Kq7GP.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/Kq7GP\",\"small_square\":\"http:\\/\\/i.imgur.com\\/Kq7GPs.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/Kq7GPl.jpg\"}},{\"image\":{\"title\":\"Rough fitting the rails to posts.\",\"caption\":\"Leveled the base and squared up the rails, fastened whitewashed T&amp;G pine to side rails.  Head and footboards fitted with 3\\/8 GIS fir.  Head and footboards are removable.\",\"hash\":\"MhFHs\",\"datetime\":\"2012-12-01 20:51:16\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2592,\"height\":1552,\"size\":616960,\"views\":104911,\"bandwidth\":64725890560},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/MhFHs.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/MhFHs\",\"small_square\":\"http:\\/\\/i.imgur.com\\/MhFHss.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/MhFHsl.jpg\"}},{\"image\":{\"title\":\"Sliding doors on each side for quick access.\",\"caption\":\"My friends and I rent rehearsal space, but I take my breakables and snare with me each week.  I built sliders into both sides to get at the small stuff.\",\"hash\":\"G9UvY\",\"datetime\":\"2012-12-01 20:51:31\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2592,\"height\":1552,\"size\":530396,\"views\":102339,\"bandwidth\":54280196244},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/G9UvY.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/G9UvY\",\"small_square\":\"http:\\/\\/i.imgur.com\\/G9UvYs.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/G9UvYl.jpg\"}},{\"image\":{\"title\":\"Fitting the sliding doors.\",\"caption\":\"\",\"hash\":\"OU4h4\",\"datetime\":\"2012-12-01 20:51:45\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2592,\"height\":1552,\"size\":493672,\"views\":100994,\"bandwidth\":49857909968},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/OU4h4.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/OU4h4\",\"small_square\":\"http:\\/\\/i.imgur.com\\/OU4h4s.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/OU4h4l.jpg\"}},{\"image\":{\"title\":\"Finished slider.\",\"caption\":\"I used a 22&quot; stainless slider to hang the door, and a 1\\/2&quot; aluminum U channel to guide the bottom.\",\"hash\":\"zBVo8\",\"datetime\":\"2012-12-01 20:52:02\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2592,\"height\":1552,\"size\":573075,\"views\":96790,\"bandwidth\":55467929250},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/zBVo8.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/zBVo8\",\"small_square\":\"http:\\/\\/i.imgur.com\\/zBVo8s.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/zBVo8l.jpg\"}},{\"image\":{\"title\":\"Gutted our old IKEA bed for parts.\",\"caption\":\"\",\"hash\":\"CGyct\",\"datetime\":\"2012-12-01 20:52:22\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2851,\"height\":1900,\"size\":435962,\"views\":97096,\"bandwidth\":42330166352},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/CGyct.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/CGyct\",\"small_square\":\"http:\\/\\/i.imgur.com\\/CGycts.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/CGyctl.jpg\"}},{\"image\":{\"title\":\"Will an 8 pc drumset fit?\",\"caption\":\"\",\"hash\":\"ttNA4\",\"datetime\":\"2012-12-01 20:52:36\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2851,\"height\":1900,\"size\":482108,\"views\":94997,\"bandwidth\":45798813676},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/ttNA4.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/ttNA4\",\"small_square\":\"http:\\/\\/i.imgur.com\\/ttNA4s.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/ttNA4l.jpg\"}},{\"image\":{\"title\":\"Yes!\",\"caption\":\"\",\"hash\":\"a87XK\",\"datetime\":\"2012-12-01 20:52:54\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2851,\"height\":1900,\"size\":508252,\"views\":93068,\"bandwidth\":47301997136},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/a87XK.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/a87XK\",\"small_square\":\"http:\\/\\/i.imgur.com\\/a87XKs.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/a87XKl.jpg\"}},{\"image\":{\"title\":\"Snare access hatch.\",\"caption\":\"\",\"hash\":\"Oc2Fc\",\"datetime\":\"2012-12-01 20:53:07\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2851,\"height\":1900,\"size\":439560,\"views\":92720,\"bandwidth\":40756003200},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/Oc2Fc.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/Oc2Fc\",\"small_square\":\"http:\\/\\/i.imgur.com\\/Oc2Fcs.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/Oc2Fcl.jpg\"}},{\"image\":{\"title\":\"Thanks Ikea!\",\"caption\":\"\",\"hash\":\"IrQjF\",\"datetime\":\"2012-12-01 20:53:22\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":2851,\"height\":1900,\"size\":500322,\"views\":92239,\"bandwidth\":46149200958},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/IrQjF.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/IrQjF\",\"small_square\":\"http:\\/\\/i.imgur.com\\/IrQjFs.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/IrQjFl.jpg\"}},{\"image\":{\"title\":\"Completed!\",\"caption\":\"\",\"hash\":\"MwBDj\",\"datetime\":\"2012-12-01 20:53:34\",\"type\":\"image\\/jpeg\",\"animated\":\"false\",\"width\":1188,\"height\":792,\"size\":211467,\"views\":91366,\"bandwidth\":19320893922},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/MwBDj.jpg\",\"imgur_page\":\"http:\\/\\/imgur.com\\/MwBDj\",\"small_square\":\"http:\\/\\/i.imgur.com\\/MwBDjs.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/MwBDjl.jpg\"}}]}}";

                HttpClient client = new DefaultHttpClient();
                try {
                    HttpGet httpGet = new HttpGet(strings[0]);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = client.execute(httpGet, responseHandler);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                setJson(result);
            }
        }.execute(toApiURL(url));
    }

    private void setJson(String json) {
        this.json = json;
        try {
            setRootObject(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setRootObject(JSONObject rootObject) throws JSONException {
        this.rootObject = rootObject;
        albumObject = rootObject.getJSONObject("album");
        imagesArray = albumObject.getJSONArray("images");
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("ImgurAlbumAdapter::getItem", "" + position + " / " + getCount());

        if (imagesArray == null)
             return null;

        try {
            JSONObject image = imagesArray.getJSONObject(position);
            return ImgurPictureFragment.newInstance(image.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        if (imagesArray == null)
            return 0;
        return imagesArray.length();
    }

    private static String toApiURL(String url) {
        String albumHash = url.substring(url.lastIndexOf("/a/") + 3);
        int index = albumHash.indexOf('/');
        if (index != -1)
            albumHash = albumHash.substring(0, index);
        return "http://api.imgur.com/2/album/" + albumHash + ".json";
    }


    private boolean hasTitlePage() {
        boolean hasTitlePage = false;

        try {
            hasTitlePage = (rootObject.get("title") != null || rootObject.get("description") == null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hasTitlePage;
    }
}
