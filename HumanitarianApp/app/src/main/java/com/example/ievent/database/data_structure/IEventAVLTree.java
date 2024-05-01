package com.example.ievent.database.data_structure;


import com.example.ievent.entity.Event;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *  The IEventAVLTree class is an extension of the AVLTree class.
 */
public class IEventAVLTree extends AVLTree<IEventData>{

    public enum keyType {
        Price,
    }


    public IEventAVLTree(IEventData value) {
        super(value);
    }

    public IEventAVLTree(IEventData value, Tree<IEventData> leftNode, Tree<IEventData> rightNode) {
        super(value, leftNode, rightNode);
    }


    /**
     * given a array of events, insert them into the AVL tree
     */
    public static IEventAVLTree insertEvents(ArrayList<Event> events, keyType type, IEventAVLTree root) {
        IEventAVLTree newRoot = root;

        for (Event event : events) {

            LinkedList<String> ids = new LinkedList<>();

            ids.add(event.getTitle());
            IEventData data = null;

            switch (type) {
                case Price:
                    data = new IEventData(event.getPrice(), ids);
                    break;
            }

            Tree<IEventData> temp = root.find(data);
            if (temp != null) {
                temp.value.addId(event.getTitle());
                newRoot = root;
            } else {
                newRoot = (IEventAVLTree) root.insert(data);
            }
        }
        return newRoot;
    }
}
