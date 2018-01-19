package uscool.io.event.addeditevent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import uscool.io.event.R;
import uscool.io.event.photoPicker.DefaultCallback;
import uscool.io.event.photoPicker.EasyImage;
import uscool.io.event.util.AppUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Main UI for the add event screen. Users can enter a event title and description.
 */
public class AddEditEventFragment extends Fragment implements AddEditEventContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditEventContract.Presenter mPresenter;

    private TextView mTitle;

    private TextView mDescription;

    private Button mBtnAddImageGallery;

    private Button mBtnAddImageCamera;

    private ImageView mPickedImageView;

    private String mImageFilePath;

    private String mUsername = "Ujjawal";

    public static AddEditEventFragment newInstance() {
        return new AddEditEventFragment();
    }

    public AddEditEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                 getActivity().findViewById(R.id.fab_edit_event_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveEvent(mUsername, mImageFilePath, mDescription.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        mBtnAddImageGallery = root.findViewById(R.id.btnAddImageGallery);
        mBtnAddImageCamera = root.findViewById(R.id.btnAddImageCamera);
        mPickedImageView = root.findViewById(R.id.pickedImage);
        mTitle =  root.findViewById(R.id.add_event_title);
        mDescription =  root.findViewById(R.id.add_event_description);

        mBtnAddImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EasyImage.canDeviceHandleGallery(getContext())) {
                    EasyImage.openGallery(getActivity().getSupportFragmentManager().findFragmentById(R.id.contentFrame), 0);
                } else {
                    Toast.makeText(getContext(), "No Gallery App Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnAddImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openCamera(getActivity().getSupportFragmentManager().findFragmentById(R.id.contentFrame), 0);
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull ArrayList<File> imageFiles, ArrayList<String> filePaths, EasyImage.ImageSource source, int type) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                File imgFile = imageFiles.get(0);
                mImageFilePath = imgFile.getAbsolutePath();
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mPickedImageView.setImageBitmap(myBitmap);
                    mPickedImageView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getContext());
                    if (photoFile != null){
                        boolean isDeleted = photoFile.delete();
                        if(isDeleted) {
                            Toast.makeText(getContext(), "File Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void initPhotoPicker() {
        EasyImage.configuration(getContext())
                .setImagesFolderName("ProjectLocker")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(false);

    }

    @Override
    public void setImage(String filepath) {
        mPickedImageView.setImageBitmap(AppUtil.getBitmapFromFilePath(filepath));
    }

    @Override
    public void showEmptyEventError() {
        Snackbar.make(mTitle, getString(R.string.empty_event_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEventsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setDescription(String description) {
        mDescription.setText(description);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
