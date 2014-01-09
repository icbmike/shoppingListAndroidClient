package mike.edu.shoppinglist;

import java.util.ArrayList;
import java.util.List;

import mike.edu.shoppinglist.ShoppingListAPIWrapper.APICall;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class ShoppingListActivity extends ListActivity implements OnItemLongClickListener {
	
    private ArrayAdapter<String> adapter;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        
        setListAdapter(adapter);
        
        getListView().setOnItemLongClickListener(this);
     
        refresh(null);
        
    }
	
	public void refresh(MenuItem menuItem){
		APICallerTask task = new APICallerTask(APICall.GET_SHOPPING_LIST);
        task.execute();
	}
	
	public void delete(MenuItem menuItem){
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Are you really sure?");
		builder.setMessage("Are you sure you want to clear the list?");
		
		builder.setPositiveButton("Do It", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				APICallerTask task = new APICallerTask(APICall.DELETE_LIST);
		        task.execute();
			}
		});
		
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
	public void addNew(MenuItem menuItem){
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add a new item?");
		final EditText editText = new EditText(this);
		builder.setView(editText);
		builder.setPositiveButton("Do It", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				APICallerTask task = new APICallerTask(APICall.ADD_ITEM, editText.getText().toString());
		        task.execute();
			}
		});
		
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_list, menu);
        return true;
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Are you sure you want to Delete this item");
		builder.setMessage("Delete " + adapter.getItem(position) + "from shopping list?");
		builder.setPositiveButton("Do It", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				APICallerTask task = new APICallerTask(APICall.DELETE_ITEM, position);
		        task.execute();
			}
		});
		
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
		return true;
	}
	
	
	private class APICallerTask extends AsyncTask<Void, Void, List<String>>{
		
		private APICall apiCall;
		private int index;
		private String newItem;
		
		public APICallerTask(ShoppingListAPIWrapper.APICall apiCall){
			this.apiCall = apiCall;
			
		}
		
		public APICallerTask(ShoppingListAPIWrapper.APICall apiCall, int index){
			this(apiCall);
			this.index = index;		
		}
		
		public APICallerTask(ShoppingListAPIWrapper.APICall apiCall, String item){
			this(apiCall);
			this.newItem = item;		
		}

		@Override
		protected void onPostExecute(List<String> result) {
			adapter.clear();
			adapter.addAll(result);
		}
		
		@Override
		protected List<String> doInBackground(Void... indexArray) {
			
			List<String> result = null;
			
			switch(apiCall){
				case ADD_ITEM:
					ShoppingListAPIWrapper.addShoppingListItem(newItem);
					break;
				case DELETE_ITEM:
					ShoppingListAPIWrapper.deleteShoppingListItem(index);
					break;
				case DELETE_LIST:
					ShoppingListAPIWrapper.deleteShoppingList();
					break;
				case GET_ITEM:
					String item = ShoppingListAPIWrapper.getShoppingListItem(index);
					result = new ArrayList<String>();
					result.add(item);
					break;
			}
			
			//Refresh the list
			result = ShoppingListAPIWrapper.getShoppingList();
			
			return result;
		}
	}
    
    
    
    
}
