package com.example.mobiledevelopment.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.ContentResolver;
import android.text.InputType;
import android.view.LayoutInflater;
import android.net.Uri;
import android.widget.EditText;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobiledevelopment.DataValidator;
import com.example.mobiledevelopment.R;
import com.google.firebase.auth.AuthCredential;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final int USER_IMAGE_HEIGHT = 200;
    private static final int USER_IMAGE_WIDTH = 250;
    private static final int REQUEST_CODE = 1;
    private EditText mNewEmail;
    private EditText mNewName;
    private TextView mCurrentName;
    private TextView mCurrentEmail;
    private ImageView mProfileImage;
    private FirebaseUser mCurrentUser;
    private Button mSaveEmailButton;
    private Button mSaveNameButton;
    private FloatingActionButton mChangeImageButton;
    private Uri mImageUri;
    private StorageReference mStorageReference;
    private StorageTask<UploadTask.TaskSnapshot> mUploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_profile, container, false);
        initClassFields(inflate);
        initSwipeRefresh(inflate.findViewById(R.id.swipe_refresh));
        mSaveNameButton.setOnClickListener(view -> changeName());
        mSaveEmailButton.setOnClickListener(view -> checkUserPassword());
        mChangeImageButton.setOnClickListener(view -> openImage());
        return inflate;
    }

    private void initClassFields(View inflate) {
        mNewName = inflate.findViewById(R.id.new_name);
        mNewEmail = inflate.findViewById(R.id.new_email);
        mCurrentName = inflate.findViewById(R.id.username);
        mCurrentEmail = inflate.findViewById(R.id.email);
        mSaveEmailButton = inflate.findViewById(R.id.button_save_email);
        mSaveNameButton = inflate.findViewById(R.id.button_save_name);
        mProfileImage = inflate.findViewById(R.id.image_profile);
        mChangeImageButton = inflate.findViewById(R.id.button_change_image);
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void initSwipeRefresh(SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.setOnRefreshListener(() -> {
            showCurrentUserDetails();
            swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mCurrentUser != null) {
            showCurrentUserDetails();
        } else {
            Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void showCurrentUserDetails() {
        mCurrentName.setText(mCurrentUser.getDisplayName());
        mCurrentEmail.setText(mCurrentUser.getEmail());
        String image = Objects.requireNonNull(mCurrentUser.getPhotoUrl()).toString();
        Picasso.get()
                .load(image)
                .resize(USER_IMAGE_WIDTH, USER_IMAGE_HEIGHT)
                .centerCrop()
                .into(mProfileImage);
    }

    private void changeName() {
        String name = mNewName.getText().toString();
        if (DataValidator.isNameValid(name)) {
            mNewName.setError(null);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name).build();
            mCurrentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.name_update_success, Toast.LENGTH_SHORT).show();
                            mNewName.getText().clear();
                        }
                    });
        } else {
            mNewName.setError(getString(R.string.name_error));
        }
    }

    private void checkUserPassword() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        EditText editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setTitle(getString(R.string.enter_password));
        dialog.setView(editText);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm),
                (dialogInterface, i) -> changeEmail(editText.getText().toString()));
        dialog.show();
    }

    private void changeEmail(String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(
                Objects.requireNonNull(mCurrentUser.getEmail()), password);
        mCurrentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            }
        });
        String email = mNewEmail.getText().toString();
        if (mCurrentUser != null && DataValidator.isEmailValid(email)) {
            sendChangeEmailRequest(email);
        } else {
            mNewEmail.setError(getString(R.string.email_error));
        }
    }

    private void sendChangeEmailRequest(String email) {
        mNewEmail.setError(null);
        mCurrentUser.updateEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), R.string.email_update_success, Toast.LENGTH_SHORT).show();
                mNewEmail.getText().clear();
            } else {
                Toast.makeText(getContext(), R.string.upload_fail, Toast.LENGTH_SHORT).show();
            }
        });
        mCurrentEmail.setText(email);
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(getContext(), R.string.upload, Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        if (mImageUri != null) {
            StorageReference storageReference = mStorageReference.child(mCurrentUser.getUid()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = storageReference.putFile(mImageUri);
            mUploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showCurrentImage(task.getResult());
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), R.string.upload_fail, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void showCurrentImage(Uri uri) {
        if (uri != null) {
            UserProfileChangeRequest profileRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri).build();
            mCurrentUser.updateProfile(profileRequest)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.image_update_success, Toast.LENGTH_SHORT).show();
                        }
                    });
            Picasso.get()
                    .load(uri.toString())
                    .resize(USER_IMAGE_WIDTH, USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(mProfileImage);
        }
    }
}
