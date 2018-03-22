package mg.studio.weatherappdesign;

import java.util.List;

/**
 * Created by RPM on 2018/3/21.
 */

public class GsonData {

    public List<HeWeather> HeWeather6;

    public class HeWeather {
        public basicDate basic;

        public nowDate now;

        public String status;

        public updateDate update;

    }

    public class basicDate {
        public String cid;
        public String location;
        public String parent_city;
        public String admin_area;
        public String cnty;
        public String lat;
        public String lon;
        public String tz;

    }

    public class nowDate {
        public String cond_code;
        public String cond_txt;
        public String fl;
        public String hum;
        public String pcpn;
        public String pres;
        public String tmp;
        public String vis;
        public String wind_deg;
        public String wind_dir;
        public String wind_sc;
        public String wind_spd;
    }

    public class updateDate {
        public String loc;
        public String utc;
    }
}
