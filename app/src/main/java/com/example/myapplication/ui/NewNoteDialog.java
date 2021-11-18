package com.example.myapplication.ui;

        import com.example.myapplication.R;
        import android.content.Context;
        import android.os.Bundle;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.fragment.app.DialogFragment;

        import org.jetbrains.annotations.Nullable;

public class NewNoteDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "NewNoteDialog";
    private ISomewhereActivity mISomewhereActivity;

    //widgets
    private EditText mTitle, mContent;
    private TextView mCreate, mCancel;

    //vars


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_note, container, false);
        mTitle = view.findViewById(R.id.note_title);
        mContent = view.findViewById(R.id.note_content);
        mCreate = view.findViewById(R.id.create);
        mCancel = view.findViewById(R.id.cancel);

        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);

        getDialog().setTitle("New Note");

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.create:{

                // insert the new note

                String title = mTitle.getText().toString();
                String content = mContent.getText().toString();

                if(!title.equals("")){

                    mISomewhereActivity.createNewNote(title,content);
                    getDialog().dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.cancel:{
                getDialog().dismiss();
                break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mISomewhereActivity = (ISomewhereActivity)getActivity();
    }

}





















