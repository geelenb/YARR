package be.geelen.yarr.postPages.Readability;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


class ReadabilityAdapter extends FragmentStatePagerAdapter {
    private JSONObject readabilityObject;
    private Document doc;
    private ArrayList<Fragment> fragments;

    public ReadabilityAdapter(FragmentManager fm, String url) {
        super(fm);

        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";

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
                setReadabilityJson(result);
            }
        }.execute(url);
    }

    private void setReadabilityJson(String json) {
        try {
            setRootObject(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setRootObject(JSONObject readabilityObject) throws JSONException {
        this.readabilityObject = readabilityObject;
        doc = Jsoup.parse(readabilityObject.getString("content"));

        fragments = new ArrayList<Fragment>();


        if (readabilityObject.getString("lead_image_url") != null) {
            fragments.add(ReadabilityPictureFragment.newInstance(
                    readabilityObject.getString("title"),
                    readabilityObject.getString("lead_image_url"),
                    readabilityObject.getString("excerpt")));
        }

        ArrayList<String> htmlParts = new ArrayList<String>();
        ArrayList<String> imgSrcs = new ArrayList<String>();
        ArrayList<String> imgAlts = new ArrayList<String>();

        Elements imgElements = doc.getElementsByTag("img");
        StringBuilder buildDoc = new StringBuilder(readabilityObject.getString("content"));

        for (Element imgElement : imgElements) {
            StringBuilder htmlPart = new StringBuilder();

            int index = buildDoc.indexOf("<img");
            htmlPart.append(buildDoc.substring(0, index));

            for (Element parent : imgElement.parents()){
                htmlPart = htmlPart.append("</" + parent.tag()+ ">");
            }

            buildDoc = new StringBuilder(buildDoc.substring(index));
            buildDoc = new StringBuilder(buildDoc.substring(buildDoc.indexOf(">") + 1));

            for (Element parent : imgElement.parents()){
                buildDoc = buildDoc.insert(0, "<" + parent.tag() + ">");
            }

            htmlParts.add(htmlPart.toString());
            imgSrcs.add(imgElement.attr("src"));
            imgAlts.add(imgElement.attr("alt") == null ? "" : imgElement.attr("alt"));
        }

        htmlParts.add(buildDoc.toString());

        for (int i = 0; i < htmlParts.size(); i++) {
            String htmlPart = htmlParts.get(i);
            if (Jsoup.parse(htmlPart).text().length() > 0) {
                fragments.add(ReadabilityHtmlFragment.newInstance(htmlPart));
            }

            if (i >= imgAlts.size())
                break;

            String imgSrc = imgSrcs.get(i);
            String imgAlt = imgAlts.get(i);

            fragments.add(ReadabilityPictureFragment.newInstance(
                    "",
                    imgSrc,
                    imgAlt));
        }


        notifyDataSetChanged();

        // check: http://readability.com/api/content/v1/parser?token=d48451fa7e61e79f0f7e215fe2258dc41b69ca75&url=http://www.techdirt.com/articles/20140815/11500128226/redditors-propose-setting-up-consumers-union-to-fight-back-against-broadband-giants.shtml
    }

    @Override
    public Fragment getItem(int position) {
//        String html = doc.outerHtml();
//        Fragment fragment = ReadabilityHtmlFragment.newInstance(html);
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        if (fragments == null)
            return 0;

        return fragments.size();
    }
}