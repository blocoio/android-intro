package io.bloco.introduction;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import io.bloco.introduction.api.SearchResponse;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
  private final List<SearchResponse.Event> events;
  private final DateFormat dateFormat;

  public EventsAdapter(List<SearchResponse.Event> events) {
    this.events = events;
    dateFormat =
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
  }

  @Override public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
    return new EventViewHolder(view);
  }

  @Override public void onBindViewHolder(EventViewHolder holder, int position) {
    SearchResponse.Event event = events.get(position);
    holder.bind(event);
  }

  @Override public int getItemCount() {
    return events.size();
  }

  class EventViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;
    private final TextView nameText;
    private final TextView locationText;
    private final TextView dateText;

    private EventViewHolder(View eventView) {
      super(eventView);
      imageView = (ImageView) eventView.findViewById(R.id.event_item_image);
      nameText = (TextView) eventView.findViewById(R.id.event_item_name);
      locationText = (TextView) eventView.findViewById(R.id.event_item_location);
      dateText = (TextView) eventView.findViewById(R.id.event_item_date);
    }

    private void bind(final SearchResponse.Event event) {
      nameText.setText(event.name.text);

      String location = event.venue.name + ", " +
          event.venue.address.city + ", " +
          event.venue.address.country;
      locationText.setText(location);

      dateText.setText(dateFormat.format(event.start.local));

      Picasso.with(itemView.getContext())
          .load(event.logo.original.url)
          .centerCrop()
          .fit()
          .into(imageView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent = new Intent(itemView.getContext(), ShareActivity.class);
          intent.putExtra(ShareActivity.EVENT_TITLE_EXTRA, event.name.text);
          itemView.getContext().startActivity(intent);
        }
      });
    }
  }
}
