package org.tasks.location;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.base.Strings;
import org.tasks.R;
import org.tasks.data.Place;
import org.tasks.data.PlaceUsage;
import org.tasks.location.LocationPickerAdapter.PlaceViewHolder;

public class LocationPickerAdapter extends ListAdapter<PlaceUsage, PlaceViewHolder> {

  private final OnLocationPicked callback;

  LocationPickerAdapter(OnLocationPicked callback) {
    super(new DiffCallback());

    this.callback = callback;
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).place.getId();
  }

  @NonNull
  @Override
  public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new PlaceViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_place, parent, false),
        callback);
  }

  @Override
  public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  public interface OnLocationPicked {
    void picked(Place place);

    void delete(Place place);
  }

  public static class PlaceViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView address;
    private final View delete;
    private Place place;

    PlaceViewHolder(@NonNull View itemView, OnLocationPicked onLocationPicked) {
      super(itemView);
      itemView.setOnClickListener(v -> onLocationPicked.picked(place));
      name = itemView.findViewById(R.id.name);
      address = itemView.findViewById(R.id.address);
      delete = itemView.findViewById(R.id.delete);
      delete.setOnClickListener(v -> onLocationPicked.delete(place));
    }

    public void bind(PlaceUsage placeUsage) {
      this.place = placeUsage.place;
      String name = place.getDisplayName();
      String address = place.getAddress();
      this.name.setText(name);
      if (Strings.isNullOrEmpty(address) || address.equals(name)) {
        this.address.setVisibility(View.GONE);
      } else {
        this.address.setText(address);
        this.address.setVisibility(View.VISIBLE);
      }
      delete.setVisibility(placeUsage.count > 0 ? View.GONE : View.VISIBLE);
    }
  }

  public static class DiffCallback extends ItemCallback<PlaceUsage> {

    @Override
    public boolean areItemsTheSame(@NonNull PlaceUsage oldItem, @NonNull PlaceUsage newItem) {
      return oldItem.place.getUid().equals(newItem.place.getUid());
    }

    @Override
    public boolean areContentsTheSame(@NonNull PlaceUsage oldItem, @NonNull PlaceUsage newItem) {
      return oldItem.equals(newItem);
    }
  }
}