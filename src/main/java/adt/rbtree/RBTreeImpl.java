package adt.rbtree;

import adt.bst.BSTImpl;
import adt.bst.BSTNode;
import adt.bt.Util;
import adt.rbtree.RBNode.Colour;

public class RBTreeImpl<T extends Comparable<T>> extends BSTImpl<T> implements RBTree<T> {

	public RBTreeImpl() {
		this.root = new RBNode<T>();
	}

	protected int blackHeight() {
		return blackHeightRec((RBNode<T>) this.root);
	}

	private int blackHeightRec(RBNode<T> node) {
		int retorno = 0;
		if (!node.isEmpty()) {
			if(node.getLeft().isEmpty()) {
				retorno = blackHeightRec((RBNode<T>) node.getRight());
			}else {
				retorno = blackHeightRec((RBNode<T>) node.getLeft());
			}
			

			if (node.getColour() == Colour.BLACK) {
				retorno++;

			}
		}
		return retorno;

	}

	protected boolean verifyProperties() {
		boolean resp = verifyNodesColour() && verifyNILNodeColour() && verifyRootColour() && verifyChildrenOfRedNodes()
				&& verifyBlackHeight();

		return resp;
	}

	/**
	 * The colour of each node of a RB tree is black or red. This is guaranteed by
	 * the type Colour.
	 */
	private boolean verifyNodesColour() {
		return true; // already implemented
	}

	/**
	 * The colour of the root must be black.
	 */
	private boolean verifyRootColour() {
		return ((RBNode<T>) root).getColour() == Colour.BLACK; // already
		// implemented
	}

	/**
	 * This is guaranteed by the constructor.
	 */
	private boolean verifyNILNodeColour() {
		return true; // already implemented
	}

	/**
	 * Verifies the property for all RED nodes: the children of a red node must be
	 * BLACK.
	 */
	private boolean verifyChildrenOfRedNodes() {
		return verifyChildrenOfRedNodesRec((RBNode<T>) this.root);
	}

	private boolean verifyChildrenOfRedNodesRec(RBNode<T> node) {
		boolean retorno = true;
		if (!node.isEmpty()) {
			if (!node.equals(root)) {
				RBNode<T> pai = (RBNode<T>) node.getParent();
				if (pai.getColour() == Colour.RED && node.getColour() != Colour.BLACK) {
					retorno = false;
				}
				if (retorno) {
					RBNode<T> left = (RBNode<T>) node.getLeft();
					RBNode<T> right = (RBNode<T>) node.getRight();
					retorno = verifyChildrenOfRedNodesRec(left) && verifyChildrenOfRedNodesRec(right);
				}
			} else {
				if (node.getColour() != Colour.BLACK) {
					retorno = false;
				}
				if (retorno) {
					RBNode<T> left = (RBNode<T>) node.getLeft();
					RBNode<T> right = (RBNode<T>) node.getRight();
					retorno = verifyChildrenOfRedNodesRec(left) && verifyChildrenOfRedNodesRec(right);
				}
			}
		}
		return retorno;

	}

	/**
	 * Verifies the black-height property from the root.
	 */
	private boolean verifyBlackHeight() {
		return verifyBlackHeightRec((RBNode<T>) getRoot());
	}

	private boolean verifyBlackHeightRec(RBNode<T> node) {
		boolean retorno = true;
		if (!node.isEmpty()) {
			int blackLeft = blackHeightRec((RBNode<T>) node.getLeft());
			int blackRight = blackHeightRec((RBNode<T>) node.getRight());
			if (node.getColour() == Colour.BLACK) {
				blackLeft++;
				blackRight++;
			}
			if (blackLeft != blackRight) {
				retorno = false;
			}
			if (retorno) {
				retorno = verifyBlackHeightRec((RBNode<T>) node.getLeft())
						&& verifyBlackHeightRec((RBNode<T>) node.getRight());
			}
		}
		return retorno;
	}

	@Override
	public void insert(T value) {

		insertRec(value, (RBNode<T>) root);

	}

	private void insertRec(T value, RBNode<T> node) {
		if (node.isEmpty()) {
			node.setData(value);
			node.setColour(Colour.RED);
			RBNode<T> left = new RBNode<>();
			RBNode<T> right = new RBNode<>();
			left.setParent(node);
			right.setParent(node);
			node.setLeft(left);
			node.setRight(right);
			fixUpCase1(node);
		} else {
			if (value.compareTo(node.getData()) > 0) {
				insertRec(value, (RBNode<T>) node.getRight());
			} else {
				insertRec(value, (RBNode<T>) node.getLeft());
			}
		}
	}

	@Override
	public RBNode<T>[] rbPreOrder() {
		RBNode<T>[] retorno = new RBNode[size()];
		if (!getRoot().isEmpty()) {
			retorno = rbPreOrderRec((RBNode<T>) getRoot());
		}

		return retorno;
	}

	private RBNode<T>[] rbPreOrderRec(RBNode<T> node) {
		RBNode<T>[] array = null;
		if (!node.isEmpty()) {
			RBNode<T> element = node;
			RBNode<T> left = (RBNode<T>) node.getLeft();
			RBNode<T> right = (RBNode<T>) node.getRight();
			RBNode<T>[] subLeft = rbPreOrderRec(left);
			RBNode<T>[] subRight = rbPreOrderRec(right);
			array = mergeArray(element, subLeft, subRight);
		}
		return array;

	}

	// private RBNode<T>[] preOrderRec(RBNode<T> node) {
	// RBNode<T>[] array = null;
	// if (!node.isEmpty()) {
	// RBNode<T> element = node;
	// RBNode<T>[] subLeft = preOrderRec(node.getLeft());
	// RBNode<T>[] subRight = preOrderRec(node.getRight());
	// array = mergeArray(element, subLeft, subRight, PRE_ORDER);
	// }
	// return array;
	// }

	private RBNode<T>[] mergeArray(RBNode<T> element, RBNode<T>[] subLeft, RBNode<T>[] subRight) {
		int tamanho = 1;
		if (subLeft != null) {
			tamanho += subLeft.length;
		}
		if (subRight != null) {
			tamanho += subRight.length;
		}
		RBNode<T>[] array = new RBNode[tamanho];
		int j;

		j = 0;

		array[j++] = element;

		if (subLeft != null) {
			for (int i = 0; i < subLeft.length; i++) {
				array[j++] = subLeft[i];
			}
		}
		if (subRight != null) {
			for (int i = 0; i < subRight.length; i++) {
				array[j++] = subRight[i];
			}
		}
		return array;

	}

	// FIXUP methods
	protected void fixUpCase1(RBNode<T> node) {
		if (node.equals(getRoot())) {
			node.setColour(Colour.BLACK);
		} else {
			fixUpCase2(node);
		}
	}

	protected void fixUpCase2(RBNode<T> node) {
		if (((RBNode<T>) node.getParent()).getColour() == Colour.RED) {
			fixUpCase3(node);
		}
	}

	protected void fixUpCase3(RBNode<T> node) {
		RBNode<T> tio = getTio(node);
		RBNode<T> pai = (RBNode<T>) node.getParent();
		if (tio.getColour() == Colour.RED) {
			pai.setColour(Colour.BLACK);
			tio.setColour(Colour.BLACK);
			((RBNode<T>) pai.getParent()).setColour(Colour.RED);
			fixUpCase1((RBNode<T>) pai.getParent());
		} else {
			fixUpCase4(node);
		}
	}

	private RBNode<T> getTio(RBNode<T> node) {
		RBNode<T> tio = null;
		RBNode<T> pai = (RBNode<T>) node.getParent();

		if (pai.getParent().getLeft().equals(pai)) {
			tio = (RBNode<T>) pai.getParent().getRight();
		} else {
			tio = (RBNode<T>) pai.getParent().getLeft();
		}
		return tio;
	}

	protected void fixUpCase4(RBNode<T> node) {
		RBNode<T> n = node;
		if (isRightChild(n) && isLeftChild((RBNode<T>) n.getParent())) {

			RBNode<T> x = (RBNode<T>) Util.leftRotation((BSTNode<T>) node.getParent());
			//atualizaPai((RBNode<T>) node.getParent(), x);
			atualizaPai(x);

			n = (RBNode<T>) n.getLeft();

		} else if (isLeftChild(n) && isRightChild((RBNode<T>) n.getParent())) {
			RBNode<T> x = (RBNode<T>) Util.rightRotation((BSTNode<T>) node.getParent());
			atualizaPai(x);

			n = (RBNode<T>) n.getRight();
		}
		fixUpCase5(n);
	}

	private void atualizaPai(RBNode<T> n) {
		BSTNode<T> pai = (BSTNode<T>) n.getParent();
		if(pai==null) {
			this.root = n;
		}else {
			if(!pai.getRight().isEmpty() && pai.getRight().equals(n.getRight())) {
				pai.setRight(n);
			}else if(!pai.getRight().isEmpty() && pai.getRight().equals(n.getLeft())) {
				pai.setRight(n);;
			}else {
				pai.setLeft(n);
			}
		}
	}

	private void atualizaPai(RBNode<T> node, RBNode<T> n) {
		BSTNode<T> pai = (BSTNode<T>) n.getParent();
		if (pai == null) {
			this.root = n;
		} else {
			if (pai.getRight() == node) {
				pai.setRight(n);
			} else {
				pai.setLeft(n);
			}
		}
	}

	private boolean isRightChild(RBNode<T> n) {
		RBNode<T> pai = (RBNode<T>) n.getParent();
		return pai.getRight().equals(n);
	}

	private boolean isLeftChild(RBNode<T> n) {
		RBNode<T> pai = (RBNode<T>) n.getParent();
		return (pai.getLeft().equals(n));
	}

	protected void fixUpCase5(RBNode<T> node) {
		RBNode<T> pai = (RBNode<T>) node.getParent();
		RBNode<T> avo = (RBNode<T>) pai.getParent();

		pai.setColour(Colour.BLACK);
		avo.setColour(Colour.RED);

		if (isLeftChild(node)) {

			RBNode<T> x = (RBNode<T>) Util.rightRotation(avo);
			//atualizaPai(avo, x);
			atualizaPai(x);

		} else {

			RBNode<T> x = (RBNode<T>) Util.leftRotation(avo);
			//atualizaPai((RBNode<T>) node.getParent(), x);
			atualizaPai(x);
		}
	}
}
