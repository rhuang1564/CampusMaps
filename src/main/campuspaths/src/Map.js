import React, {Component} from 'react';
import "./Map.css";

class Map extends Component {

  // NOTE:
  // This component is a suggestion for you to use, if you would like to.
  // It has some skeleton code that helps set up some of the more difficult parts
  // of getting <canvas> elements to display nicely.
  //
  // If you don't want to use this component, you're free to delete it.

  constructor(props) {
    super(props);
    this.backgroundImage = new Image();
    this.canvasReference = React.createRef();
    this.backgroundImage.onload = () => {
      this.drawBackgroundImage();
    };
    this.backgroundImage.src = "campus_map.jpg";
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    this.drawBackgroundImage();
    this.drawPath();
  }

  drawBackgroundImage() {
    let canvas = this.canvasReference.current;
    let ctx = canvas.getContext("2d");
    //
    if (this.backgroundImage.complete) { // This means the image has been loaded.
      canvas.width = this.backgroundImage.width;
      canvas.height = this.backgroundImage.height;
      ctx.drawImage(this.backgroundImage, 0, 0);
    }
  }

  drawPath = () => {
    console.log("drawing path")
    let canvas = this.canvasReference.current;
    let ctx = canvas.getContext("2d");
    ctx.strokeStyle = "red";
    ctx.lineWidth = 10;
    this.props.path.forEach((curr) => {
      console.log(curr)
      ctx.moveTo(curr["x1"],curr["y1"]);
      ctx.lineTo(curr["x2"],curr["y2"]);
      ctx.stroke();
    })
  }

  render() {
    return (
      <div className="canvasHolder">
        <canvas ref={this.canvasReference}/>
      </div>
    )
  }
}

export default Map;