package targetClass;

/**
 * 人クラス
 */
public class Person {

	/** 身長 */
	private double hight = 155;

	/**
	 * 挨拶を表示する
	 */
	public void sayHello() {
		System.out.println("Hello!");
	}

	/**
	 * 引数の食べ物を食べる。
	 * 
	 * @param food 食べ物
	 * @param num  食事回数
	 */
	public void eat(String food, int num) {

		// 食べ物を表示する
		System.out.println("私は" + food + "を" + num + "回食べた。");
	}

	/**
	 * 身長を伸ばし、身長を表示する
	 * 
	 * @param length 長さ(cm)
	 */
	public void grow(double length) {
		this.hight += length;
		System.out.println("私は身長が" + this.hight + "cmになった。");
	}
}
