import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;


public class test{
    // 全局HttpClient:
    static HttpClient httpClient = HttpClient.newBuilder().build();
    public static void main(String[] args) throws Exception {
        String url = "https://covid-api.mmediagroup.fr/v1/cases";
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .header("User-Agent", "Java HttpClient").header("Accept", "*/*")
                .timeout(Duration.ofSeconds(5))
                .version(Version.HTTP_2).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, List<String>> headers = response.headers().map();
        NA_ALL na = null;
        NA_P na_p = null;
        LinkedList<NA_ALL> NA_List = new LinkedList<>();
        String str = response.body().toString();
        boolean flag1 = false, flag2 = false;
        int counter = 0;
        for (String Retval : str.split("\"All\":|}|,|\\{")) {
            String retval = Retval.trim();
            if (retval.equals("")||retval.equals("null")) continue;
            //System.out.println(retval);
            if (retval.equals("\"China\":")||retval.equals("\"Japan\":")||retval.equals("\"United Kingdom\":")
                    ||retval.equals("\"US\":"))
            {
                flag1 = true;
            }
            if (retval.equals("\"Anhui\":")||retval.equals("\"Aichi\":")||retval.equals("\"Anguilla\":")
                    ||retval.equals("\"Mississippi\":"))
            {
                flag2 = true;
            }
            if (retval.equals("\"Colombia\":")||retval.equals("\"Jordan\":")||retval.equals("\"Uruguay\":")
                    ||retval.equals("\"Global\":"))
            {
                flag1 = false;
                flag2 = false;
                counter = 0;
                NA_List.add(na);
            }
            if (flag1){
                 if(counter == 0) {
                     na = new NA_ALL();
                     na.setName(retval.substring(0,retval.length()-1));
                     counter++;
                 }
                 else if(counter == 1) {
                     na.setConfirmed(retval);
                     counter++;
                 }
                 else if(counter == 2) {
                     na.setRecovered(retval);
                     counter++;
                 }
                 else if(counter == 3) {
                     na.setDeaths(retval);
                     counter++;
                 }
                 else if(counter == 4) {
                     na.setCountry(retval);
                     counter++;
                 }
                 else if(counter == 5) {
                     na.setPopulation(retval);
                     flag1 = false;
                     counter = 0;
                     continue;
                 }
            }
            if (flag2){
                if (counter == 0){
                    na_p  = new NA_P();
                    na_p.setName("\'"+retval+"\'");
                    counter++;
                }
                else if (counter == 1){
                    na_p.setLat("\'"+retval+"\'");
                    counter++;
                }
                else if(counter == 2){
                    na_p.setLong("\'"+retval+"\'");
                    counter++;
                }
                else if(counter == 3){
                    na_p.setConfirmed("\'"+retval+"\'");
                    counter++;
                }
                else if(counter == 4){
                    na_p.setRecovered("\'"+retval+"\'");
                    counter++;
                }
                else if(counter == 5){
                    na_p.setDeaths("\'"+retval+"\'");
                    counter++;
                }
                else if(counter == 6){
                    na_p.setUpdated("\'"+retval+"\'");
                    na.P_List.add(na_p);
                    counter = 0;
                }
            }
        }
        /*String s = "[";
        for (NA_ALL Naall : NA_List) {
            s = s+Naall.toString();
        }
        s = s+"]";
        System.out.println(s);/*

         */
        for (NA_ALL Na : NA_List) {
            for (NA_P Nap : Na.P_List)
            System.out.println(Nap.toString());
        }
}

static class NA_P{
    private String name;
    private String lat;
    private String Long;
    private String confirmed;
    private String recovered;
    private String deaths;
    private String updated;
    public void setName(String name){
        this.name = name;
    }
    public void setLat(String lat){
        this.lat = lat;
    }
    public void setLong(String Long){
        this.Long = Long;
    }
    public void setConfirmed(String confirmed){
        this.confirmed = confirmed;
    }
    public void setRecovered(String recovered){
        this.recovered = recovered;
    }
    public void  setDeaths(String deaths){
        this.deaths  = deaths;
    }
    public void setUpdated(String updated){
        this.updated = updated;
    }
@Override
public String toString() {
    return  "("+name + "," + confirmed + "," + recovered + "," + deaths+','+updated+"),";
}
}
static class NA_ALL{
    private String name;
    private String confirmed;
    private String recovered;
    private String deaths;
    private String country;
    private String population;
    LinkedList<NA_P> P_List = new LinkedList<>();
    public void setName(String name){
        this.name = name;
    }
    public void setConfirmed(String confirmed){
        this.confirmed = confirmed;
    }
    public void setRecovered(String recovered){
        this.recovered = recovered;
    }
    public void setDeaths(String deaths){
        this.deaths = deaths;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public void setPopulation(String population){
        this.population = population;
    }
    @Override
    public String toString() {
        return "{\"country\" : " + name + "," + confirmed + "," + recovered + "," + population + "}";
    }
}}