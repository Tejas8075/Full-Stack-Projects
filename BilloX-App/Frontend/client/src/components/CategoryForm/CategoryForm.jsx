import React, { useContext, useEffect, useState } from 'react'
import { assets } from '../../assets/assets';
import toast from 'react-hot-toast';
import { addCategory } from '../../service/CategoryService';
import { AppContext } from '../../context/AppContext';

const CategoryForm = () => {

  const {categories, setCategories} = useContext(AppContext);

  const [loading, setLoading] = useState(false);
  const [image, setImage] = useState(false);
  const [data, setData] = useState({
    name: "",
    description: "",
    bgColor: "#2c2c2c"
  });

  useEffect(() => {
    console.log(data);
  }, [data])

  const onChangeHandler = (e) => {
    const value = e.target.value;
    const name = e.target.name;

    setData((data) => ({...data, [name]: value}))
  }

  const onSubmitHandler = async (e) => {
    e.preventDefault();

    if(!image){
      toast.error("Select image for category");
      return;
    }
    
    setLoading(true);
    
    const formData = new FormData();
    formData.append("category", JSON.stringify(data));
    formData.append("file", image);

    try {
      const response = await addCategory(formData);

      if(response.status === 201){
        setCategories([...categories, response.data]);

        toast.success("Category added successfully");

        setData({
          name: "",
          description: "",
          bgColor: "2c2c2c"
        })

        setImage(false)
      }
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    } finally{
      setLoading(false);
    }
  }

  return (
    <div className='mx-2 mt-2'>

      <div className="row">

        <div className="card col-md-12 form-container">
          <div className="card-body">
            <form onSubmit={onSubmitHandler}>
              <div className="mb-3">
                <label htmlFor="image" className='form-label'>
                  <img src={image ? URL.createObjectURL(image) : assets.upload}alt="" 
                  width={55} 
                  />

                  <input type="file" name='image' id='image' className='form-control' 
                  hidden 
                  onChange={(e) => setImage(e.target.files[0])}
                  />
                </label>
              </div>

              <div className="mb-3">
                <label htmlFor="name" className='form-label'>Name</label>

                <input type="text" name='name'
                  id='name'
                  className='form-control'
                  placeholder='Category Name'
                  onChange={onChangeHandler}
                  value={data.name}
                />
              </div>

              <div className="mb-3">
                <label htmlFor="description" className='form-label'>Description</label>

                <textarea
                  rows='5'
                  name='description'
                  id='description'
                  className='form-control'
                  placeholder='Write content here'
                  onChange={onChangeHandler}
                  value={data.description}
                ></textarea>
              </div>

              <div className="mb-3">
                <label htmlFor="bgColor" className='form-label'>Background color</label>

                <br/>

                <input type="color"
                  name='bgColor'
                  id='bgColor'
                  placeholder='#ffffff'
                  onChange={onChangeHandler}
                  value={data.bgColor}
                />
              </div>

              <button 
              className="btn btn-warning w-100" type='submit'
              disabled={loading}
              >{loading ? "Loading..." : "Submit"}</button>
            </form>
          </div>
        </div>

      </div>

    </div>
  )
}

export default CategoryForm