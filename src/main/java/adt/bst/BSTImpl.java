package adt.bst;

import adt.bt.BTNode;

public class BSTImpl<T extends Comparable<T>> implements BST<T> {

   protected BSTNode<T> root;

   protected static final int PRE_ORDER = 0;
   private static final int ORDER = 1;
   private static final int POS_ORDER = 2;

   public BSTImpl() {
      root = new BSTNode<T>();
   }

   public BSTNode<T> getRoot() {
      return this.root;
   }

   @Override
   public boolean isEmpty() {
      return root.isEmpty();
   }

   @Override
   public int height() {
      int retorno = -1;
      if (!this.isEmpty()) {
         retorno = heightRec(root);
      }
      return retorno;
   }

   protected int heightRec(BTNode<T> node) {
      int retorno = 0;
      if (node.isEmpty()) {
         retorno = -1;
      } else {
         retorno++;
         int hl = heightRec(node.getLeft());
         int hr = heightRec(node.getRight());
         if (hl > hr) {
            retorno += hl;
         } else {
            retorno += hr;
         }
      }
      return retorno;
   }

   @Override
   public BSTNode<T> search(T element) {
      return searchRec(element, getRoot());
   }

   private BSTNode<T> searchRec(T element, BSTNode<T> node) {
      BSTNode<T> retorno = new BSTNode<>();
      if (!node.isEmpty()) {
         if (node.getData().equals(element)) {
            retorno = node;
         } else {
            if (node.getData().compareTo(element) > 0) {
               retorno = searchRec(element, (BSTNode<T>) node.getLeft());
            } else {
               retorno = searchRec(element, (BSTNode<T>) node.getRight());
            }
         }
      }
      return retorno;
   }

   @Override
   public void insert(T element) {
      insertRec(getRoot(), element);
   }

   protected void insertRec(BSTNode<T> node, T element) {
      if (node.isEmpty()) {
         node.setData(element);

         BSTNode<T> left = new BSTNode.Builder().parent(node).build();
         BSTNode<T> right = new BSTNode.Builder().parent(node).build();
         node.setLeft(left);
         node.setRight(right);
      } else {
         if (node.getData().compareTo(element) > 0) {
            insertRec((BSTNode<T>) node.getLeft(), element);
         } else {
            insertRec((BSTNode<T>) node.getRight(), element);
         }
      }
   }

   @Override
   public BSTNode<T> maximum() {
      BSTNode<T> retorno = null;
      if (!this.isEmpty()) {
         retorno = maximumRec(getRoot());
      }
      return retorno;
   }

   protected BSTNode<T> maximumRec(BSTNode<T> node) {
      BSTNode<T> retorno = null;
      if (node.isEmpty()) {
         retorno = (BSTNode<T>) node.getParent();
      } else {
         retorno = maximumRec((BSTNode<T>) node.getRight());
      }
      return retorno;
   }

   @Override
   public BSTNode<T> minimum() {
      BSTNode<T> retorno = null;
      if (!this.isEmpty()) {
         retorno = minimunRec(getRoot());
      }
      return retorno;
   }

   protected BSTNode<T> minimunRec(BSTNode<T> node) {
      BSTNode<T> retorno = null;
      if (node.isEmpty()) {
         retorno = (BSTNode<T>) node.getParent();
      } else {
         retorno = minimunRec((BSTNode<T>) node.getLeft());
      }
      return retorno;
   }

   @Override
   public BSTNode<T> sucessor(T element) {
      BSTNode<T> retorno = null;
      if (!this.isEmpty()) {
         BSTNode<T> node = search(element);
         if (!node.getRight().isEmpty()) {
            retorno = minimunRec((BSTNode<T>) node.getRight());
         } else {
            if (node.getParent() != null) {

               while (node != null && node.getData().compareTo(element) <= 0) {
                  node = (BSTNode<T>) node.getParent();
               }

               if (node != null) {
                  retorno = node;
               }
            }
         }
      }
      return retorno;
   }

   @Override
   public BSTNode<T> predecessor(T element) {
      BSTNode<T> retorno = null;

      if (!this.isEmpty()) {
         BSTNode<T> node = search(element);
         if (!node.getLeft().isEmpty()) {
            retorno = maximumRec((BSTNode<T>) node.getLeft());
         } else {
            if (node.getParent() != null) {
               while (node != null && node.getData().compareTo(element) >= 0) {
                  node = (BSTNode<T>) node.getParent();
               }
               if (node != null) {
                  retorno = node;
               }
            }
         }
      }

      return retorno;
   }

   @Override
   public void remove(T element) {
      BSTNode<T> node = search(element);
      if (!node.isEmpty()) {
         removeNode(node);
      }
   }

   protected void removeNode(BSTNode<T> node) {
      if (node.isLeaf()) {
         zerar(node);
      } else if (oneSon(node)) {
         if (node.getLeft().isEmpty()) {
            BSTNode<T> suc = minimunRec((BSTNode<T>) node.getRight());
            node.setData(suc.getData());
            removeNode(suc);
         } else {
            BSTNode<T> pre = maximumRec((BSTNode<T>) node.getLeft());
            node.setData(pre.getData());
            removeNode(pre);
         }
      } else {
         BSTNode<T> suc = minimunRec((BSTNode<T>) node.getRight());
         node.setData(suc.getData());
         removeNode(suc);
      }
   }

   protected void zerar(BSTNode<T> node) {
      node.setData(null);
      node.setRight(null);
      node.setLeft(null);
   }

   protected boolean oneSon(BSTNode<T> node) {
      boolean retorno = false;
      if (node.getLeft().isEmpty() && !node.getRight().isEmpty()) {
         retorno = true;
      } else if (!node.getLeft().isEmpty() && node.getRight().isEmpty()) {
         retorno = true;
      }
      return retorno;
   }

   @Override
   public T[] preOrder() {
      T[] retorno = (T[]) new Comparable[size()];
      if (!getRoot().isEmpty()) {
         retorno = preOrderRec(getRoot());
      }
      return retorno;
   }

   private T[] preOrderRec(BTNode<T> node) {
      T[] array = null;
      if (!node.isEmpty()) {
         T element = node.getData();
         T[] subLeft = preOrderRec(node.getLeft());
         T[] subRight = preOrderRec(node.getRight());
         array = mergeArray(element, subLeft, subRight, PRE_ORDER);
      }
      return array;
   }

   protected T[] mergeArray(T element, T[] subLeft, T[] subRight, int ordem) {
      int tamanho = 1;
      if (subLeft != null) {
         tamanho += subLeft.length;
      }
      if (subRight != null) {
         tamanho += subRight.length;
      }
      T[] array = (T[]) new Comparable[tamanho];
      int j;
      switch (ordem) {

      case PRE_ORDER:
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
         break;
      case ORDER:
         j = 0;
         if (subLeft != null) {
            for (int i = 0; i < subLeft.length; i++) {
               array[j++] = subLeft[i];
            }
         }

         array[j++] = element;

         if (subRight != null) {
            for (int i = 0; i < subRight.length; i++) {
               array[j++] = subRight[i];
            }
         }
         break;
      case POS_ORDER:
         j = 0;

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

         array[j++] = element;
         break;
      }

      return array;
   }

   @Override
   public T[] order() {
      T[] retorno = (T[]) new Comparable[size()];
      if (!getRoot().isEmpty()) {
         retorno = orderRec(getRoot());
      }
      return retorno;
   }

   private T[] orderRec(BTNode<T> node) {
      T[] array = null;
      if (!node.isEmpty()) {
         T[] subLeft = orderRec(node.getLeft());
         T element = node.getData();
         T[] subRight = orderRec(node.getRight());
         array = mergeArray(element, subLeft, subRight, ORDER);
      }
      return array;
   }

   @Override
   public T[] postOrder() {
      T[] retorno = (T[]) new Comparable[size()];
      if (!getRoot().isEmpty()) {
         retorno = postOrderRec(getRoot());
      }
      return retorno;
   }

   private T[] postOrderRec(BTNode<T> node) {
      T[] array = null;
      if (!node.isEmpty()) {
         T[] subLeft = postOrderRec(node.getLeft());
         T[] subRight = postOrderRec(node.getRight());
         T element = node.getData();
         array = mergeArray(element, subLeft, subRight, POS_ORDER);
      }
      return array;
   }

   /**
    * This method is already implemented using recursion. You must understand how
    * it work and use similar idea with the other methods.
    */
   @Override
   public int size() {
      return size(root);
   }

   private int size(BSTNode<T> node) {
      int result = 0;
      // base case means doing nothing (return 0)

      if (!node.isEmpty()) { // indusctive case
         // System.out.println(node.getData());
         result = 1 + size((BSTNode<T>) node.getLeft()) + size((BSTNode<T>) node.getRight());
      }
      return result;
   }

}
