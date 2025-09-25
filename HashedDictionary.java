import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashedDictionary<K, V> implements DictionaryInterface <K, V>
{
    // The dictionary:
    private int numberOfEntries;
    private long collisionCount = 0;
    private static final int DEFAULT_CAPACITY = 5;     // Must be prime
    private static final int MAX_CAPACITY = 100000;

    // The hash table:
    private HashNode[] hashTable;
    private int tableSize;                             // Must be prime
    private static final int MAX_SIZE = 2 * MAX_CAPACITY;
    private boolean integrityOK = false;
    private static final double MAX_LOAD_FACTOR = 0.8; // Fraction of hash table
    // that can be filled

    public HashedDictionary()
    {
        this(DEFAULT_CAPACITY); // Call next constructor
    } // end default constructor

    public HashedDictionary(int initialCapacity)
    {
        initialCapacity = checkCapacity(initialCapacity);
        numberOfEntries = 0;    // Dictionary is empty



        // Set up hash table:
        // Initial size of hash table is same as initialCapacity if it is prime;
        // otherwise increase it until it is prime size
        int tableSize = getNextPrime(initialCapacity);
        checkSize(tableSize);   // Check that size is not too large

        // The cast is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        HashNode<K, V>[] temp = (HashNode<K, V>[])new HashNode[tableSize];
        hashTable = temp;
        Arrays.fill(hashTable, null);
        integrityOK = true;
    } // end constructor

    // -------------------------
    // We've added this method to display the hash table for illustration and testing
    // -------------------------
    public void displayHashTable()
    {
        checkIntegrity();
        for (int index = 0; index < hashTable.length; index++)
        {
            if (hashTable[index] == null)
                System.out.println("null ");
            else
            {
                HashNode<K, V> current = hashTable[index];
                while(current != null)
                {
                    System.out.println(current.getKey() + " " + current.getValue());
                    current = current.getNext();
                }
            }
        } // end for
        System.out.println();
    } // end displayHashTable
    public long getCollisionCount() {
        return collisionCount;
    }




    // ----------------------------------FOR LINEAR PROBING------------------------------------

/*

    public void add(K key, V value) {
        // Ensure value is of type MediaItem
        MediaItem newMediaItem = (MediaItem) value;

        checkIntegrity(); // Ensure the table is ready for use
        if ((key == null) || (newMediaItem == null)) {
            throw new IllegalArgumentException("Cannot add null key or value to the dictionary.");
        }

        int index = PAFhash(key) % hashTable.length; // Calculate the hash index
        int originalIndex = index; // Save the original index for detecting cycles
        boolean added = false;

        while (!added) {
            if (hashTable[index] == null) {
                // Insert the new item if the slot is empty
                HashNode<K, MediaItem> newNode = new HashNode<>(key, newMediaItem);
                hashTable[index] = newNode;
                added = true;
                numberOfEntries++;
            } else if (hashTable[index].getKey().equals(key)) {
                // Key already exists, update the entry
                MediaItem currentItem = (MediaItem) hashTable[index].getValue();
                currentItem.addPlatform(newMediaItem.getPlatform(), newMediaItem.getAvailableCountries());
                hashTable[index].setValue(currentItem);
                added = true;
            } else {
                // Collision, probe to the next index
                collisionCount++;
                index = (index + 1) % hashTable.length;

                // Detect full cycle to prevent infinite looping
                if (index == originalIndex) {
                    throw new IllegalStateException("Hash table is full. Cannot add new item.");
                }
            }
        }

        if (isHashTableTooFull())
            resize();

    }
    public V remove(K key) {
        checkIntegrity();
        if (key == null) {
            throw new IllegalArgumentException("Cannot remove null key from the dictionary.");
        }
        V removedItem = null;

        int index = PAFhash(key) % hashTable.length;
        int originalIndex = index;
        boolean found = false;

        while (!found) {
            if (hashTable[index] == null) {
                // Key not found
                return null;
            } else if (hashTable[index].getKey().equals(key)) {
                // Key found, remove the entry
                removedItem = (V) hashTable[index];
                hashTable[index] = null;
                found = true;
            } else {
                // Collision, continue probing
                index = (index + 1) % hashTable.length;

                // Detect full cycle to prevent infinite looping
                if (index == originalIndex) {
                    return null;
                }
            }
        }
        numberOfEntries--;
        return removedItem;
    }

    public MediaItem get(K key){
        checkIntegrity();
        if (key == null) {
            throw new IllegalArgumentException("Cannot get null key from the dictionary.");
        }
        int index = PAFhash(key) % hashTable.length;
        int originalIndex = index;
        boolean found = false;

        while(!found){
            if (hashTable[index] == null) {
                System.out.println("Media not found!");
                break;
                // Key not found
            } else if (hashTable[index].getKey().equals(key)) {
                // Key found, output value
                found = true;
                return (MediaItem) hashTable[index].getValue();
            } else {
                // Collision, continue probing
                index = (index + 1) % hashTable.length;

                // Detect full cycle to prevent infinite looping
                if (index == originalIndex) {
                    System.out.println("Media not found!");
                    break;
                }
            }
        }
        return null;
    }
*/
    //------------------------------------END OF LINEAR PROBING-------------------------------------------------pupu



    //---------------------------------------DOUBLE HASHING-----------------------------------------------------
    public void add(K key, V value) {
    // Ensure value is of type MediaItem
    MediaItem newMediaItem = (MediaItem) value;

    checkIntegrity();
    if ((key == null) || (newMediaItem == null)) {
        throw new IllegalArgumentException("Cannot add null key or value to the dictionary.");
    }

    int index = PAFhash(key) % hashTable.length;
    int step = hashForDouble(key) % hashTable.length;
    if (step == 0) step = 1; // Ensure non-zero step

    int originalIndex = index;
    int attempts = 0;
    boolean added = false;

    while (!added && attempts < hashTable.length) {
        if (hashTable[index] == null) {
            HashNode<K, MediaItem> newNode = new HashNode<>(key, newMediaItem);
            hashTable[index] = newNode;
            added = true;
            numberOfEntries++;
        } else if (hashTable[index].getKey().equals(key)) {
            MediaItem currentItem = (MediaItem) hashTable[index].getValue();
            currentItem.addPlatform(newMediaItem.getPlatform(), newMediaItem.getAvailableCountries());
            hashTable[index].setValue(currentItem);
            added = true;
        } else {
            collisionCount++;
            index = (index + step) % hashTable.length;
            attempts++;
        }
    }

    if (!added) {
        throw new IllegalStateException("Hash table is full. Cannot add new item.");
    }

    if (isHashTableTooFull())
        resize();
}

public V remove(K key) {
   checkIntegrity();
   if (key == null) {
       throw new IllegalArgumentException("Cannot remove null key from the dictionary.");
   }

   V removedItem = null;

   int index = PAFhash(key) % hashTable.length;
   int step = hashForDouble(key) % hashTable.length;
   if (step == 0) step = 1; // Ensure non-zero step

   int originalIndex = index;
   int attempts = 0;
   boolean found = false;

   while (!found && attempts < hashTable.length) {
       if (hashTable[index] == null) {
           // Key not found
           return null;
       } else if (hashTable[index].getKey().equals(key)) {
           // remove
           removedItem = (V) hashTable[index];
           hashTable[index] = null;
           found = true;
           numberOfEntries--;
       } else {
           // probing
           index = (index + step) % hashTable.length;
           attempts++;
       }
   }
   return removedItem;
}
public MediaItem get(K key){
    checkIntegrity();
    if ((key == null)) {
        throw new IllegalArgumentException("Cannot search null key from the dictionary.");
    }

    int index = PAFhash(key) % hashTable.length;
    int step = hashForDouble(key) % hashTable.length;
    if (step == 0) step = 1; // Ensure non-zero step

    int originalIndex = index;
    int attempts = 0;
    boolean found = false;

    while (!found && attempts < hashTable.length) {
        if (hashTable[index] == null) {
            // Key not found
            System.out.println("Media Not Found");
            return null;
        } else if (hashTable[index].getKey().equals(key)) {
            // Key found, remove the entry
            found = true;
            return (MediaItem) hashTable[index].getValue();
        } else {
            // Continue probing
            index = (index + step) % hashTable.length;
            attempts++;
        }
    }
    return null;
}


    //--------------------------------------END OF DOUBLE HASH-----------------------------------------------



    public boolean contains(K key)
    {
        return get(key) != null;
    } // end contains

    public boolean isEmpty()
    {
        return numberOfEntries == 0;
    } // end isEmpty

    public int getSize()
    {
        return numberOfEntries;
    } // end getSize

    public final void clear()
    {
        checkIntegrity();
        for (int index = 0; index < hashTable.length; index++)
            hashTable[index] = null;

        numberOfEntries = 0;
    } // end clear

    public Iterator<K> getKeyIterator()
    {
        return new KeyIterator();
    } // end getKeyIterator

    public Iterator<V> getValueIterator()
    {
        return new ValueIterator();
    } // end getValueIterator



    private int SSFhash(K key){
        String strKey = key.toString();
        strKey = strKey.toLowerCase();

        int result = 0;
        for(int i = 0; i < strKey.length(); i++){
            result += strKey.charAt(i) - 'a' + 1;;
        }
        return Math.abs(result);

    }
    public int hashForDouble(K key) {
        int k = SSFhash(key);
        int q = getNextPrime(hashTable.length/2);
        return q - (k % q);
    }
    private int PAFhash(K key){
        String strKey=key.toString();
        int result = 0;
        int z = 33;
        for(int i = 0; i < strKey.length(); i++){
            result += (int) (strKey.charAt(i) * Math.pow(z, strKey.length() - 1 - i));
        }
        return Math.abs(result);
    }
    private void resize()
    {
        HashNode<K, V>[] oldTable = hashTable;
        int oldSize = hashTable.length;
        int newSize = getNextPrime(oldSize + oldSize);
        checkSize(newSize);

        // The cast is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        HashNode<K, V>[] tempTable = (HashNode<K, V>[])new HashNode[newSize]; // Increase size of array
        hashTable = tempTable;
        numberOfEntries = 0;
        // Reset number of dictionary entries, since
        // it will be incremented by add during rehash

        // Rehash dictionary entries from old array to the new and bigger array;
        // skip both null locations and removed entries
        for (int index = 0; index < oldSize; index++)
        {
            if (oldTable[index] != null)
            {
                HashNode<K, V> current = oldTable[index];
                add(current.getKey(), current.getValue());
            }
        } // end for
    } // end resize

    // Returns true if lambda > MAX_LOAD_FACTOR for hash table;
    // otherwise returns false.
    private boolean isHashTableTooFull()
    {
        return numberOfEntries > MAX_LOAD_FACTOR * hashTable.length;
    } // end isHashTableTooFull

    // Returns a prime integer that is >= the given integer.
    private int getNextPrime(int integer)
    {
        // if even, add 1 to make odd
        if (integer % 2 == 0)
        {
            integer++;
        } // end if

        // test odd integers
        while (!isPrime(integer))
        {
            integer = integer + 2;
        } // end while

        return integer;
    } // end getNextPrime

    // Returns true if the given intege is prime.
    private boolean isPrime(int integer)
    {
        boolean result;
        boolean done = false;

        // 1 and even numbers are not prime
        if ( (integer == 1) || (integer % 2 == 0) )
        {
            result = false;
        }

        // 2 and 3 are prime
        else if ( (integer == 2) || (integer == 3) )
        {
            result = true;
        }

        else // integer is odd and >= 5
        {
            assert (integer % 2 != 0) && (integer >= 5);

            // a prime is odd and not divisible by every odd integer up to its square root
            result = true; // assume prime
            for (int divisor = 3; !done && (divisor * divisor <= integer); divisor = divisor + 2)
            {
                if (integer % divisor == 0)
                {
                    result = false; // divisible; not prime
                    done = true;
                } // end if
            } // end for
        } // end if

        return result;
    } // end isPrime

    // Throws an exception if this object is not initialized.
    private void checkIntegrity()
    {
        if (!integrityOK)
            throw new SecurityException ("HashedDictionary object is corrupt.");
    } // end checkIntegrity

    // Ensures that the client requests a capacity
    // that is not too small or too large.
    private int checkCapacity(int capacity)
    {
        if (capacity < DEFAULT_CAPACITY)
            capacity = DEFAULT_CAPACITY;
        else if (capacity > MAX_CAPACITY)
            throw new IllegalStateException("Attempt to create a dictionary " +
                    "whose capacity is larger than " +
                    MAX_CAPACITY);
        return capacity;
    } // end checkCapacity

    // Throws an exception if the hash table becomes too large.
    private void checkSize(int size)
    {
        if (tableSize > MAX_SIZE)
            throw new IllegalStateException("Dictionary has become too large.");
    } // end checkSize

    private class KeyIterator implements Iterator<K>
    {
        private int currentIndex; // Current position in hash table
        private int numberLeft;   // Number of entries left in iteration
        private HashNode<K, V> currentNode;

        private KeyIterator()
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        } // end default constructor

        public boolean hasNext()
        {
            return numberLeft > 0;
        } // end hasNext

        public K next()
        {
            K result = null;

            if (hasNext())
            {
                if(currentNode != null)
                {
                    result = currentNode.getKey();
                    numberLeft--;
                    currentNode = currentNode.getNext();
                }
                else
                {
                    // Skip table locations that do not contain a hashnode
                    while (hashTable[currentIndex] == null)
                    {
                        currentIndex++;
                    } // end while

                    currentNode = hashTable[currentIndex];
                    result = currentNode.getKey();
                    numberLeft--;
                    currentNode = currentNode.getNext();
                }
                if(currentNode == null) currentIndex++;
            }
            else
                throw new NoSuchElementException();

            return result;
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end KeyIterator

    private class ValueIterator implements Iterator<V>
    {
        private int currentIndex;
        private int numberLeft;
        private HashNode<K, V> currentNode;

        private ValueIterator()
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        } // end default constructor

        public boolean hasNext()
        {
            return numberLeft > 0;
        } // end hasNext

        public V next()
        {
            V result = null;

            if (hasNext())
            {
                if(currentNode != null)
                {
                    result = currentNode.getValue();
                    numberLeft--;
                    currentNode = currentNode.getNext();
                }
                else
                {
                    // Skip table locations that do not contain a hashnode
                    while (hashTable[currentIndex] == null)
                    {
                        currentIndex++;
                    } // end while

                    currentNode = hashTable[currentIndex];
                    result = currentNode.getValue();
                    numberLeft--;
                    currentNode = currentNode.getNext();
                }
                if(currentNode == null) currentIndex++;
            }
            else
                throw new NoSuchElementException();

            return result;
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end ValueIterator

    public class HashNode<K,V> {
        private K key;
        private V value;
        private HashNode<K, V> next;

        public HashNode(K key, V value)
        {
            this.key=key;
            this.value=value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value =  value;
        }

        public HashNode<K, V> getNext() {
            return this.next;
        }

        public void setNext(HashNode<K, V> next) {
            this.next = next;
        }
    }
} // end HashedDictionary
