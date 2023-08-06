package touchfish.socialgood.search;

import java.util.ArrayList;

import touchfish.socialgood.ItemModel;


/**
 * LitExp: it is extended from the abstract class Exp,
 * 		   This class is used to represented the expression of 32-bit unsigned integer
 *
 * You are not required to implement any function inside this class.
 * Please do not change any thing inside this class as well.
 */

public class IntExp extends Exp {
	
	private Double value;

	public IntExp(Double value) {
		this.value = value;
	}

	@Override
	public String show() {
		return value.toString();
	}


	@Override
	public Double identify(ArrayList<ItemModel> itemlist) {
		return value;
	}

}
