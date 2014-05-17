package be.kuleuven.chi.app;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.categories.Goal;

/**
 * Created by NeleR on 17/05/2014.
 */
public class AddGoalPageActivity extends BaseActivity {

    Goal goal;
    int goalActivityType;

    public void showDeleteConfirmation(){
        final Dialog dialog = new Dialog(AddGoalPageActivity.this, R.style.myBackgroundStyle);
        dialog.setContentView(R.layout.popup);

        // set the dialog text and image
        dialog.setTitle(getResources().getString(R.string.delete_confirmation_title));
        TextView text = (TextView) dialog.findViewById(R.id.popup_text);
        text.setText(getResources().getString(R.string.delete_confirmation, goal.getName()));
        dialog.findViewById(R.id.popup_image).setVisibility(View.GONE);

        // buttons
        dialog.findViewById(R.id.popup_ok).setVisibility(View.GONE);
        dialog.findViewById(R.id.popup_delete).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.popup_cancel).setVisibility(View.VISIBLE);

        // cancel button
        dialog.findViewById(R.id.popup_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // just close the popup and do nothing when 'cancel' is clicked
                dialog.dismiss();
            }
        });

        // delete button
        final AppContent content = AppContent.getInstance(this);
        dialog.findViewById(R.id.popup_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete the goal when 'delete' is clicked
                content.deleteCurrentGoal();
                content.deleteBackUpCurrentGoal();
                dialog.dismiss();

                Intent intent = new Intent(AddGoalPageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(AddGoalPageActivity.this!= null && !AddGoalPageActivity.this.isFinishing()){
            dialog.show();
        }
    }
}
