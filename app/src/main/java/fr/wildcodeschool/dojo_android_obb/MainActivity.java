package fr.wildcodeschool.dojo_android_obb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import fr.wildcodeschool.dojo_android_obb.json.JsonParser;
import fr.wildcodeschool.dojo_android_obb.list.Item;
import fr.wildcodeschool.dojo_android_obb.list.ItemFragment;
import fr.wildcodeschool.dojo_android_obb.obb.ObbManager;
import fr.wildcodeschool.dojo_android_obb.obb.ObbManagerListener;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static android.os.storage.OnObbStateChangeListener.*;

public class MainActivity extends AppCompatActivity
  implements ItemFragment.OnItemClickListener, ObbManagerListener {
  // TAG
  private static final String TAG = "MainActivity";

  // ObbStateChange values
  private static final SparseArray<String> OBB_STATE = new SparseArray<>();
  static {
    OBB_STATE.put(ERROR_ALREADY_MOUNTED,    "OBB ERROR_ALREADY_MOUNTED");
    OBB_STATE.put(ERROR_COULD_NOT_MOUNT,    "OBB ERROR_COULD_NOT_MOUNT");
    OBB_STATE.put(ERROR_COULD_NOT_UNMOUNT,  "OBB ERROR_COULD_NOT_UNMOUNT");
    OBB_STATE.put(ERROR_INTERNAL,           "OBB ERROR_INTERNAL");
    OBB_STATE.put(ERROR_NOT_MOUNTED,        "OBB ERROR_NOT_MOUNTED");
    OBB_STATE.put(ERROR_PERMISSION_DENIED,  "OBB ERROR_PERMISSION_DENIED");
    OBB_STATE.put(MOUNTED,                  "OBB MOUNTED");
    OBB_STATE.put(UNMOUNTED,                "OBB UNMOUNTED");
  }
  private static final String UNDEFINED_ERROR = "UNDEFINED_ERROR";

  // OBB
  private ObbManager mObbManager;

  /**
   * Called when the activity is starting.
   * @param savedInstanceState Bundle: If the activity is being re-initialized after previously
   *                           being shut down then this Bundle contains the data it most recently
   *                           supplied in onSaveInstanceState(Bundle).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // mount the OBB file
    mObbManager = new ObbManager(this, this);
    if (RESULT_OK == mObbManager.requestReadObbPermission()) {
      // Permission has been granted
      if (!mObbManager.isObbMounted()) {
        mObbManager.mountMainObb();
      }
    }
  }

  /**
   * Used for receiving notifications from StorageManager about OBB file states.
   * @param path String: path to the OBB file the state change has happened on
   * @param state int: the current state of the OBB
   */
  @Override
  public void onObbStateChange(String path, int state) {
    TextView lStateTextView = findViewById(R.id.state_text_view);

    int lStateTextViewColor = R.color.colorError;
    lStateTextView.setText(OBB_STATE.get(state, UNDEFINED_ERROR));

    if (MOUNTED == state) {
      // Get the file from OBB mounted path
      File file = new File(mObbManager.getFilePath("data.json"));

      // try with statement works here because FileInputStream
      // implement Closeable interface.
      try (FileInputStream lFileInputStream = new FileInputStream(file)) {
        // Parse JSON content and update the recyclerView
        JsonParser.getInstance().readJsonStream(lFileInputStream);
        addRecyclerViewItems();
      } catch (IOException e) { e.printStackTrace(); }

      // Set the logger color to green
      lStateTextViewColor = R.color.colorMounted;
    }
    else if (UNMOUNTED == state) {
      // Set the logger color to orange
      lStateTextViewColor = R.color.colorUnMounted;
    }
    // Set the logger color
    lStateTextView.setTextColor(getResources().getColor(lStateTextViewColor));
  }

  /**
   * update the content of RecyclerView
   */
  private void addRecyclerViewItems() {
    // Loop on the Json items
    for (JsonParser.Song item: JsonParser.getInstance().getSongList()) {
      Bitmap lBitmap = BitmapFactory.decodeFile(mObbManager.getFilePath(item.cover));
      ItemFragment.addItem(new Item(item.artist, item.title, lBitmap));
    }
    ItemFragment.notifyDataSetChanged();
  }

  /**
   * Callback for the result from requesting permissions.
   * This method is invoked for every call on requestPermissions
   * @param requestCode int: The request code passed in requestPermissions
   * @param permissions String: The requested permissions. Never null.
   * @param grantResults int: The grant results for the corresponding permissions.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String permissions[],
                                         @NonNull int[] grantResults) {
    if (requestCode == ObbManager.PERMISSIONS_REQUEST_READ_OBB) {
      if (grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission has been granted
        mObbManager.mountMainObb();
        Log.i(TAG, "OBB_PERMISSION GRANTED");
      } else {
        Log.e(TAG, "OBB_PERMISSION REFUSED");
        finish();
      }
    }
  }

  /**
   * Perform any final cleanup before an activity is destroyed
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    // If obbManager always exists and is always mounted
    if (null != mObbManager && mObbManager.isObbMounted()) {
      mObbManager.unmountMainObb();
    }
  }

  /**
   * Called when an item of the RecyclerView emit a click event
   * @param item Item: The item in the RecyclerView related to the emit event
   */
  @Override
  public void onItemClick(Item item) {
    Toast.makeText(this, item.getArtist(), Toast.LENGTH_SHORT).show();
  }
}
