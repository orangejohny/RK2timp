/*
Forward list contains string elements
*/
#ifndef FORWARDLIST_H_
#define FORWARDLIST_H_

#include <iostream>

class intForwardList
{
private:
	/*
	Struct Node is the struct definition nestled in the class.
	Node will be the type, which we can use to declaration component
	of class or use in class method.
	*/
	struct Node
	{
		// Node data
		int value;

		// Next node pointer 
		struct Node * next;

		Node(const int& element, Node *ptr = nullptr) : 
			//value(std::move(element)),
			value(element),
			next(std::move(ptr))
		{
		}
	};

	// The front of the list
	Node * head; 

	// The rear of the list
	Node * tail;

	// The current number of elements 
	int currentNumberOfElements;

	// 
	void clear()
	{
		Node * temp;
		while (head != nullptr)
		{
			temp = head;
			head = head->next;
			delete temp;
		}
	}
public:
	// Constructor 
	intForwardList() :
		head(nullptr),
		tail(nullptr),
		currentNumberOfElements(0)
		{};

	~intForwardList() { clear(); };

	// Check if the object is empty
	bool isEmpty()
	{
		return head == nullptr;
	}

	// Return number of elements 
	const int& listCount() const
	{
		return currentNumberOfElements;
	}

	// Add element on the end of the list
	void addToTail(const int &item); 

	// Add element on the head of the list
	void addToHead(const int &item);

	// Delete and return element from the head of the list 
	int deleteFromHead();   

	// Delete and return element from the head of the list
	int deleteFromTail();

	// Delete element from the list
	void deleteElement(const int &valueDelete);

	// Show contents of the list
	void show() const;

	// Check if the value is in the list
	bool isInList(const int &value);
};

#endif
