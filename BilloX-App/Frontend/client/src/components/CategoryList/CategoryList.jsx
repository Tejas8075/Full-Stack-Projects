import React, { useContext, useState } from 'react'
import { AppContext } from '../../context/AppContext'
import './categoryList.css'
import { deleteCategory } from '../../service/CategoryService';
import toast from 'react-hot-toast';

const CategoryList = () => {

  const { categories, setCategories } = useContext(AppContext);

  const [searchTerm, setSearchTerm] = useState("");

  // Filter the Categories where first convert the category name from API to lowercase and then see if it includes the words from searchTerm state which is converted to Lowercase
  const filteredCategories = categories.filter(category => category.name.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const deleteByCategoryId = async (categoryId) => {
    try {
      
     const response = await deleteCategory(categoryId);

     if(response.status === 204){
      const updatedCategories = categories.filter(category => category.categoryId !== categoryId);

      setCategories(updatedCategories);

      toast.success("Category deleted")
     } else{
      toast.error("Unable to delete the category")
     }

    } catch (error) {
      console.error(error)
      toast.error(error.message);
    }
  }

  return (
    <div className='category-list-container'
      style={{ height: '100vh', overflowY: 'auto', overflowX: 'hidden' }}
    >
      <div className="row pe-2">
        {/* search bar */}
        <div className="input-group mb-3">
          <input
            type="text"
            id='keyword' placeholder='Search by keyword' className='form-control'
            onChange={(e) => setSearchTerm(e.target.value)}
            value={searchTerm}
          />

          <span className="input-group-text bg-warning">
            <i className="bi bi-search"></i>
          </span>
        </div>
      </div>

      <div className="row g-3 pe-2">
        {filteredCategories.map((category, index) => (
          <div key={index} className='col-12'>
            <div className="card p-3"
              style={{ backgroundColor: category.bgColor }}
            >
              <div className="d-flex align-items-center">
                <div style={{ marginRight: '15px' }}>
                  <img src={category.imgUrl}
                    alt={category.name}
                    className='category-image'
                  />
                </div>

                <div className="flex-grow-1">
                  <h5 className='mb-1 text-white'>{category.name}
                  </h5>

                  <p className="mb-0 text-white">
                    {category.items}
                  </p>
                </div>

                <div>
                  <button className='btn btn-danger btn-sm'
                  onClick={() => deleteByCategoryId(category.categoryId)}
                  >
                    <i className="bi bi-trash"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default CategoryList