// queue.cpp -- implementacje metod klas Queue i Customer
#include "forwardList.hpp"



// Add element on the end of the list
void intForwardList::addToTail(const int &item)
{
	if (tail != nullptr)
	{
		tail->next = new Node(item);
		tail = tail->next;
	}
	else
		head = tail = new Node(item);
}

// Add element on the head of the list
void intForwardList::addToHead(const int &item)
{
	head = new Node(item, head);
	if (tail==nullptr)
	{
		tail = head;
	}
}

// Delete and return element from the head of the list 
int intForwardList::deleteFromHead()
{
	int valueRet = head->value;
	Node *temp = head;
	if (head == tail)
	{
		head = tail = nullptr;
	}
	else
		head = head->next;
	delete temp;
	return valueRet;
}

// Delete and return element from the head of the list 
int intForwardList::deleteFromTail()
{
	int el = tail->value;
	if (head == tail) { // One Node
		delete head;
		head = tail = nullptr;
	}
	else // More then one Node
	{
		Node *temp;
		for (temp = head; temp->next != tail; temp = temp->next);
		delete tail;
		tail = temp;
		tail->next = 0;
	}
	return el;
}		

void intForwardList::deleteElement(const int &valueDelete)
{
	if (head!=nullptr){
		if (head == tail && valueDelete == head->value) {
			delete head;
			head = tail = nullptr;
		}
		else if (valueDelete==head->value){
			Node *temp = head;
			head = tail = nullptr;
		}
		else {
			Node *pred, *temp;
			for (pred = head, temp = head->next; temp != nullptr && !(temp->value == valueDelete); pred = pred->next, temp = temp->next);
			if (temp != nullptr) {
				pred->next = temp->next;
				if (temp == tail)
					tail = pred;
				delete temp;
			}
		}
	}
}

bool intForwardList::isInList(const int &value)
{
	Node* temp;
	for (temp = head; temp != nullptr && !(temp->value == value); temp = temp->next);
	return temp != nullptr;
}

// Show contents of the list
void intForwardList::show() const
{
	for (Node *temp = head; temp!=nullptr; temp=temp->next)
		std::cout << temp->value << " ";
	std::cout << std::endl;
}
