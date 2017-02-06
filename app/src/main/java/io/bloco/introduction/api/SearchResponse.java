package io.bloco.introduction.api;

import java.util.Date;
import java.util.List;

public class SearchResponse {

  public List<Event> events;

  public class Event {

    public TextField name;
    public TextField description;
    public LogoField logo;
    public DateField start;
    public DateField end;
    public Venue venue;

    public class TextField {
      public String text;
      public String html;
    }

    public class DateField {
      public Date local;
    }

    public class LogoField {
      public ImageField original;

      public class ImageField {
        public String url;
        public int width;
        public int height;
      }
    }

    public class Venue {
      public String name;
      public Address address;

      public class Address {
        public String city;
        public String country;
      }
    }
  }
}
