import CategoryForm from '../../components/CategoryForm/CategoryForm'
import CategoryList from '../../components/CategoryList/CategoryList'
import './manageCategory.css'

const ManageCategory = () => {
  return (
    <div className='category-container text-light'>
      <div className="left-column">
        {/* category-form */}
        <CategoryForm />
      </div>
      <div className="right-column">
        {/* list of categories */}
        <CategoryList />
      </div>
    </div>
  )
}

export default ManageCategory