import React from 'react'
import './manageItems.css'
import ItemForm from '../../components/ItemForm/ItemForm'
import ItemList from '../../components/ItemList/ItemList'

export const ManageItems = () => {
  return (
    <div className='items-container text-light'>
      <div className="left-column">
        {/* item-form */}
        <ItemForm />
      </div>
      <div className="right-column">
        {/* list of items */}
        <ItemList />
      </div>
    </div>
  )
}
