package com.slippery.sps.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.slippery.sps.R;
import com.slippery.sps.models.Post;
import com.slippery.sps.providers.AuthProvider;
import com.slippery.sps.providers.ImageProvider;
import com.slippery.sps.providers.PostProvider;
import com.slippery.sps.utils.FileUtil;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File mImageFile2;
    Button mButtonPost;
    ImageProvider mImageProvider;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    TextInputEditText mtextInputTitle;
    TextInputEditText mtextInputDescription;
    ImageView mImageViewProduccion;
    ImageView mImageViewFilm;
    ImageView mImageViewComposicion;
    ImageView mImageViewBeatMaker;
    CircleImageView McircleImageBack;
    TextView mTextViewCategory;


    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2 = 2;
    String mCategory = "" ;
    String mTitle = "";
    String mDescription = "";
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();
        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mtextInputTitle = findViewById(R.id.textInputTitle);
        mtextInputDescription = findViewById(R.id.textInputDescription);
        mImageViewProduccion = findViewById(R.id.ImageViewProduccion);
        mImageViewFilm = findViewById(R.id.ImageViewFilm);
        mImageViewComposicion = findViewById(R.id.ImageViewComposicion);
        mImageViewBeatMaker = findViewById(R.id.ImageViewBeatMaker);
        mTextViewCategory = findViewById(R.id.textViewCategory);










        mButtonPost = findViewById(R.id.btnPost);
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPost();
            }
        });

        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(GALLERY_REQUEST_CODE);
            }
        });mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(GALLERY_REQUEST_CODE_2);
            }
        });

        mImageViewProduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Produccion";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Film";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewComposicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Composicion";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewBeatMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "BeatMaker";
                mTextViewCategory.setText(mCategory);
            }
        });


    }

    private void clickPost() {
        mTitle = mtextInputTitle.getText().toString();
        mDescription = mtextInputDescription.getText().toString();
        if (!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            if (mImageFile != null){
                saveImage();
            }
            else {
                Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, "Completa los campos para publicar", Toast.LENGTH_SHORT).show();
        }






    }

    private void saveImage() {
        mDialog.show();
        mImageProvider.save(PostActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
               if (task.isSuccessful()){
                   mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                          final String url = uri.toString();

                           mImageProvider.save(PostActivity.this, mImageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                   if (taskImage2.isSuccessful()){
                                       mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                           @Override
                                           public void onSuccess(Uri uri2) {
                                               String url2 = uri.toString();
                                               Post post = new Post();
                                               post.setImage1(url);
                                               post.setImage2(url2);
                                               post.setTitle(mTitle);
                                               post.setDescription(mDescription);
                                               post.setCategory(mCategory);
                                               post.setIdUser(mAuthProvider.getUid());
                                               mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> taskSave) {
                                                       mDialog.dismiss();
                                                       if (taskSave.isSuccessful()){
                                                           clearForm();
                                                           Toast.makeText(PostActivity.this, "La información se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                                       }
                                                       else {
                                                           Toast.makeText(PostActivity.this, "No se pudo almacenar la informacion", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               });
                                           }
                                       });
                                   }
                                   else {
                                       mDialog.dismiss();
                                       Toast.makeText(PostActivity.this, "La imagen numero 2 no se pudo guardar", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });

                       }
                   });

               }
               else {
                   mDialog.dismiss();
                   Toast.makeText(PostActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_LONG).show();
               }
            }
        });

    }

    private void clearForm() {
        mtextInputTitle.setText("");
        mtextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.uploadpurple);
        mImageViewPost2.setImageResource(R.drawable.uploadpurple);
        mTitle = "";
        mDescription = "";
        mCategory = "";
        mImageFile = null;
        mImageFile2 = null;

    }


    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            mImageFile = FileUtil.from(PostActivity.this, result.getData().getData());
                            mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));

                        }catch (Exception e){
                            Log.d("Error", "Se produjo un error" + e.getMessage());
                            Toast.makeText(PostActivity.this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
    );
    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
   //     startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);
    }

//    @Override
 //   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
   //     super.onActivityResult(requestCode, resultCode, data);
     //   if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
       //     try {
         //       mImageFile = FileUtil.from(this, data.getData());
           //     mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));

           // }catch (Exception e){
            //    Log.d("Error", "Se produjo un error" + e.getMessage());
            //    Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
           // }
      //  }
 //   }
}