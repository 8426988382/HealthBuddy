package com.example.hackoverflow;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class chat_Fragment extends Fragment {

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;

    int flag = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.chat_fragment, container, false);

        listView = v.findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.left);
        listView.setAdapter(chatArrayAdapter);

        if(savedInstanceState == null){
            chatArrayAdapter.add(new ChatMessage(true , "Hey There !"));
            chatArrayAdapter.add(new ChatMessage(true , "How can We Help You?"));
            chatArrayAdapter.add(new ChatMessage(true , "Have Some Questions? Ask!"));
        }
        buttonSend = v.findViewById(R.id.send);





        chatText = v.findViewById(R.id.msg);


        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });


            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                   sendChatMessage();

                   side = false;
                }
            });




        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        return v;
    }


    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");

        return true;
    }
}