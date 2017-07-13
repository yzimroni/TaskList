package net.yzimroni.tasklist.menu.builder;

@FunctionalInterface
public interface MenuRowAlign {

	public static MenuRowAlign LEFT = (number, total) -> {
		return new int[] { 0, total - number };
	};

	public static MenuRowAlign CENTER = (number, total) -> {
		int empty = total - number;
		int half = empty / 2;
		return new int[] { half, empty % 2 == 0 ? half : half + 1 };
	};

	public static MenuRowAlign RIGHT = (number, total) -> {
		return new int[] { total - number, 0 };
	};

	public int[] calculateAlign(int number, int total);

}
