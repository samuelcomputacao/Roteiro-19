package adt.bt;

import adt.bst.BSTNode;

public class Util {


	/**
	 * A rotacao a esquerda em node deve subir e retornar seu filho a direita
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> BSTNode<T> leftRotation(BSTNode<T> node) {
		BSTNode<T> pivot = (BSTNode<T>) node.getRight();
		BSTNode<T> left = (BSTNode<T>) pivot.getLeft();
		pivot.setParent(node.getParent());
		pivot.setLeft(node);
		node.setParent(pivot);
		node.setRight(left);
		if (left != null) {
			left.setParent(node);
		}

		return pivot;
	}

	/**
	 * A rotacao a direita em node deve subir e retornar seu filho a esquerda
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> BSTNode<T> rightRotation(BSTNode<T> node) {
		BSTNode<T> pivot = (BSTNode<T>) node.getLeft();
		BSTNode<T> right = (BSTNode<T>) pivot.getRight();
		pivot.setParent(node.getParent());
		pivot.setRight(node);
		node.setParent(pivot);
		node.setLeft(right);
		if (right != null) {
			right.setParent(node);
		}
		return pivot;
	}

	public static <T extends Comparable<T>> T[] makeArrayOfComparable(int size) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) new Comparable[size];
		return array;
	}
}
