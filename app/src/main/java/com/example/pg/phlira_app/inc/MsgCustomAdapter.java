package com.example.pg.phlira_app.inc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pg.phlira_app.R;
import com.example.pg.phlira_app.msg.ReadMsgData;

import java.util.ArrayList;

/**
 * Created by pg on 2017-05-02.
 * 작성자 : 서봉교
 * 커스텀 ArrayAdapter
 */

public class MsgCustomAdapter extends BaseAdapter {
    private ArrayList<String[]> m_list;

    public MsgCustomAdapter(){
        m_list = new ArrayList<String[]>();
    }

    @Override
    public int getCount() {
        return m_list.size();
    }

    @Override
    public Object getItem(int position) {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView        asub    = null;
        TextView        acont    = null;
        TextView        adate   = null;

        //Button          btn     = null;
        CustomHolder    holder  = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_list_1, parent, false);
        }

        asub    = (TextView) convertView.findViewById(R.id.asub);
        acont    = (TextView) convertView.findViewById(R.id.acont);
        adate    = (TextView) convertView.findViewById(R.id.adate);
        ImageView   pimg    = (ImageView) convertView.findViewById(R.id.msg_new);
        Typeface face = Typeface.createFromAsset(context.getAssets(), SettingVar.FONT_FILE);
        asub.setTypeface(face);
        acont.setTypeface(face);
        adate.setTypeface(face);
        //btn     = (Button) convertView.findViewById(R.id.btn_test);

        // 홀더 생성 및 Tag로 등록
        holder = new CustomHolder();
        holder.m_TextView   = asub;
        holder.m_TextView2   = acont;
        holder.m_TextView3  = adate;
        holder.m_img  = pimg;
        //holder.m_Btn        = btn;
        convertView.setTag(holder);

        // Text 등록
        //num,id,kind,subject,msg,wdate

        asub.setText(m_list.get(position)[3]);
        acont.setText(m_list.get(position)[4]);
        adate.setText(m_list.get(position)[6]);

        //Log.d("sbg_test","읽음 유무 : "+m_list.get(position)[7]);

        //해당 메세지를 읽었을 경우 'N' 아이콘 안보이게 함
        if(m_list.get(position)[7].equals("Y")){
            pimg.setVisibility(View.INVISIBLE);
        }else{
            pimg.setVisibility(View.VISIBLE);
        }

        final String pos1 = m_list.get(position)[0];
        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                ImageView   aimg    = (ImageView) v.findViewById(R.id.msg_new);
                aimg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(context, ReadMsgData.class);
                intent.putExtra("num",pos1);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.anim_left_slide, R.anim.anim_right_slide);
            }
        });

        // 리스트 아이템을 길게 터치 했을 떄 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : "+m_list.get(pos), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(String[] _msg) {
        m_list.add(_msg);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_list.remove(_position);
    }

    private class CustomHolder {
        TextView    m_TextView;
        TextView    m_TextView2;
        TextView    m_TextView3;
        ImageView   m_img;
    }
}
