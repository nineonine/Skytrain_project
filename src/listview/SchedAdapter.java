package listview;

import java.util.HashMap;
import java.util.List;

import com.douglas.skytrainproject.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SchedAdapter extends BaseExpandableListAdapter {

	private Context ctx;
	private HashMap<String, List<String>> Train_Category;
	private List<String> Train_List;
	
	public SchedAdapter(Context ctx, HashMap<String, List<String>> Movies_Category, List<String> Movies_List)
	{
		this.ctx = ctx;
		this.Train_Category = Movies_Category;
		this.Train_List = Movies_List;
	}
	
	@Override
	public int getGroupCount() {
		
		return Train_List.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return Train_Category.get(Train_List.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return Train_List.get(groupPosition);
	}

	@Override
	public Object getChild(int parent, int child) {
		
		return Train_Category.get(Train_List.get(parent)).get(child);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int parent, int child) {
		
		return child;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int parent, boolean isExpanded,
			View convertView, ViewGroup parentview) {
		String group_title = (String) getGroup(parent);
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.parent_layout, parentview, false);
			
		}
		TextView parent_textview = (TextView) convertView.findViewById(R.id.parent_txt);
		parent_textview.setTypeface(null, Typeface.BOLD);
		parent_textview.setText(group_title);
		return convertView;
	}

	@Override
	public View getChildView(int parent, int child,
			boolean lastChild, View convertView, ViewGroup parentview) {
		String child_tile = (String) getChild(parent, child);
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.child_layout, parentview, false);
		}
		TextView child_textview = (TextView) convertView.findViewById(R.id.child_txt);
		child_textview.setText(child_tile);
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
