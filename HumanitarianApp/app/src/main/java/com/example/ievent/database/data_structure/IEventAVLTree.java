package com.example.ievent.database.data_structure;


import com.example.ievent.entity.Event;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 *  The IEventAVLTree class is an extension of the AVLTree class.
 */
public class IEventAVLTree extends AVLTree<IEventData>{

    public enum KeyType {
        PRICE,
    }


    public IEventAVLTree(IEventData value) {
        super(value);
    }


    public IEventAVLTree(IEventData value, Tree<IEventData> leftNode, Tree<IEventData> rightNode) {
        super(value, leftNode, rightNode);
    }

    @Override
    public IEventAVLTree insert(IEventData element) {
        AVLTree<IEventData> avlTree = (AVLTree<IEventData>) super.insert(element);
        return new IEventAVLTree(avlTree.value, avlTree.leftNode, avlTree.rightNode);
    }

    @Override
    public AVLTree<IEventData> delete(IEventData element) {
        AVLTree<IEventData> avlTree = (AVLTree<IEventData>) super.delete(element);
        return new IEventAVLTree(avlTree.value, avlTree.leftNode, avlTree.rightNode);
    }

    /**
     * given a array of events, insert them into the AVL tree
     * @param events the array of events
     * @param type the type of key
     */
    public IEventAVLTree insertEvents(ArrayList<Event> events, KeyType type) {
        IEventAVLTree newRoot = this;
        IEventAVLTree oldRoot;

        for (Event event : events) {
            oldRoot = newRoot;

            LinkedList<String> ids = new LinkedList<>();

            ids.add(event.getTitle());
            IEventData data = null;
            switch (type) {
                case PRICE:
                    data = new IEventData(event.getPrice(), ids);
                    break;
            }

            Tree<IEventData> temp = oldRoot.find(data);
            if (temp != null && !(temp instanceof EmptyAVL)) {
                // TODO: change it to ids
                temp.value.addId(event.getTitle());
                newRoot = oldRoot;
            } else {
                newRoot = oldRoot.insert(data);
            }
        }
        return newRoot;
    }


    /**
     *  get the event ids in the range of offered key based min and max
     *  @param min the minimum value
     *  @param max the maximum value
     */
    public ArrayList<String> getEventIdsInRange(double min, double max) {
        ArrayList<String> result = new ArrayList<>();
        Stack<Tree<IEventData>> stack = new Stack<>();
        Tree<IEventData> current = this;

        while (!stack.isEmpty() || !(current instanceof EmptyAVL)) {
            if (!(current instanceof EmptyAVL)) {
                stack.push(current);
                current = current.leftNode;
            } else {
                Tree<IEventData> node = stack.pop();
                if (node.value.getKey() >= min && node.value.getKey() <= max) {
                    result.addAll(node.value.getIds());
                }
                if(node.value.getKey() > max) {
                    break;
                }
                current = node.rightNode;
            }
        }
        return result;
    }


    /**
     * delete the event by id reference
     * @param eventId the id of the event
     * @param price the price of the event
     * @return the new root of the tree
     * @throws IllegalArgumentException if the event is not in the tree
     */
    public IEventAVLTree deleteByIdRef(String eventId, double price){
        IEventData data = new IEventData(price, new LinkedList<>());

        IEventAVLTree newRoot = this;

        Tree<IEventData> temp = this.find(data);
        if(temp != null && !(temp instanceof EmptyAVL)){
            assert temp.value != null;
            temp.value.removeId(eventId);
            if(temp.value.getIds().isEmpty()){
                newRoot = (IEventAVLTree) this.delete(data);
            }
        }else{
            throw new IllegalArgumentException("The event is not in the tree");
        }
        return newRoot;
    }
}
