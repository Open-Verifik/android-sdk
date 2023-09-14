package co.verifik.verifikkit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import co.verifik.verifikkit.models.ClickServiceInterface;
import co.verifik.verifikkit.models.VerifikService;

public class VerifikServiceAdapter extends RecyclerView.Adapter<VerifikServiceAdapter.ViewHolder> {

    private VerifikService[] localDataSet;
    private ClickServiceInterface serviceInterface;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView groupTextView;
        private final TextView titleTextView;
        private final TextView descTextView;
        private final TextView timeTextView;
        private final Button exploreButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            groupTextView = (TextView) view.findViewById(R.id.group_textview);
            titleTextView = (TextView) view.findViewById(R.id.title_textview);
            descTextView = (TextView) view.findViewById(R.id.desc_textview);
            timeTextView = (TextView) view.findViewById(R.id.time_textview);
            exploreButton = (Button) view.findViewById(R.id.button_explore);
        }

        public TextView getGroupTextView() {
            return groupTextView;
        }
        public TextView getTitleTextView() {
            return titleTextView;
        }
        public TextView getDescTextView() {
            return descTextView;
        }
        public TextView getTimeTextView() {
            return timeTextView;
        }
        public Button getExploreButton() {
            return exploreButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public VerifikServiceAdapter(ClickServiceInterface serviceInterface, VerifikService[] dataSet) {
        this.serviceInterface = serviceInterface;
        this.localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_verifik_service, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getGroupTextView().setText(localDataSet[position].getGroup());
        viewHolder.getTitleTextView().setText(localDataSet[position].getTitle());
        viewHolder.getDescTextView().setText(localDataSet[position].getDescription());
        viewHolder.getTimeTextView().setText(localDataSet[position].getTime());
        viewHolder.getExploreButton().setOnClickListener(v -> {
            serviceInterface.clickOnService(localDataSet[position]);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}

