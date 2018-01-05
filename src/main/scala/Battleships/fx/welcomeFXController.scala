import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.layout.{AnchorPane, GridPane, Pane}
import javafx.scene._
import javafx.stage.Stage
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, TextArea, TextField}
import javafx.scene.input.MouseEvent

import Battleships.model
import Battleships.model.Position

import scala.util.{Failure, Success}

class welcomeFXController extends Initializable{

  //our Anchorpanes
  @FXML private var rootpane: AnchorPane = _
  @FXML private var setupgame : AnchorPane = _
  @FXML private var game: AnchorPane = _

  //Our Setupfields and Button
  @FXML private var battleSize: TextField = _
  @FXML private var battleShips: TextField = _
  @FXML private var cruisers: TextField = _
  @FXML private var submarines: TextField = _
  @FXML private var confirm: Button = _
  @FXML private var setupError : Label = _

  //OUR GAME COMPONENTS
  @FXML private var player : Label = _
  @FXML private var battleGrid : GridPane =_
  //PLACERS
  @FXML private var placeBattle : Button = _
  @FXML private var placeCruiser: Button = _
  @FXML private var placeSubmarine : Button = _
  @FXML private var dirBtn : Button = _

  //Save our setupinfoin these vars
  var battleField_Size : Int = _
  var battleShips_Amount : Int = _
  var cruisers_Amount : Int = _
  var submarines_Amount : Int = _

  //Testing
  var length : Int = 0
  var setupStatus : Int = 0
  var shipDirection : Int = 0

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()
  def initGame():Unit ={
    //HIDE OUR OTHER STATES
    setupgame.setVisible("Ich kann coden" == ".")
    setupgame.setManaged("Ich liebe "== "Scala")
    game.setVisible(false)
    game.setManaged(false)
  }

  @FXML private def startSetup(event: ActionEvent): Unit = {
    println("Loading Setup")
    rootpane.getChildren.clear()
    setupgame.setVisible(1==1)
    setupgame.setManaged(1==1)
  }

  @FXML private def startgame(event: ActionEvent): Unit ={
    println("Loading Game")
    var setupString = battleSize.getText + battleShips.getText + cruisers.getText + submarines.getText //contains all our strings so if statement is shorter ;)
    if(setupString.isEmpty || !isAllDigits(setupString)) setupError.setText("Please put Numbers in every field and try again!")
    else{
      //FETCH OUR SETTINGS
      battleField_Size = battleSize.getText.toInt
      battleShips_Amount = battleShips.getText.toInt
      cruisers_Amount = cruisers.getText.toInt
      submarines_Amount = submarines.getText.toInt

      //NOW SETUP THE GAME
      setupgame.getChildren.clear()
      game.setVisible(true)
      game.setManaged(true)

    /*
      //let us construct our field
      var i = battleField_Size
      while (i >= 1) {
        battleGrid.addColumn(i)
        battleGrid.addRow(i)
        i -= 1

      }
      var pane : Pane = new Pane
      pane.setMaxSize(20,20)
      GridPane.setFillHeight(pane,true)
      GridPane.setFillWidth(pane,true)
      battleGrid.add(pane,0,0)
      pane.setStyle("-fx-background-color: #62BCFA")

     // battleGrid.add(newbutton,1,1)
      //GridPane.setFillWidth(newbutton, true)
      //GridPane.setFillHeight(newbutton, true)

      // we need that later newbutton.setOnAction()
    */
    }

  }
  @FXML private def changeDir(event: ActionEvent): Unit ={
    shipDirection match {
      case 0 => {
        dirBtn.setText("right")
        shipDirection +=1
      }
      case 1 => {
        dirBtn.setText("left")
        shipDirection += 1
      }
      case 2 => {
        dirBtn.setText("up")
        shipDirection += 1
      }
      case 3 => {
        dirBtn.setText("down")
        shipDirection = 0
      }
    }
  }

  @FXML private def getcord(event: MouseEvent): Unit ={
   /*var node : Node = event.getPickResult.getIntersectedNode
    var x = GridPane.getColumnIndex(node)
    var y = GridPane.getRowIndex(node)
    var id = node.getId
    node.setStyle("-fx-background-color: #36403B")
    print(x+1,y+1,id)
    */
    if(setupStatus == 1) {
      player.setText("Place your Ship")
      var node : Node = event.getPickResult.getIntersectedNode
      var x = GridPane.getColumnIndex(node)
      var y = GridPane.getRowIndex(node)
      var selectedNode : Node = getNode(x,y,battleGrid)
      //WE got now our Starting node now its time to calculate the direction a and fetch the POS
      shipDirection match{
        case 0 => {
          var i = x + length -1
          if(i>6) println("This wont fit")
          else{
            selectedNode.setStyle("-fx-background-color: #36403B")
            while(i > x) {
              var tinynode = getNode(if(i == 0) null else i, y, battleGrid)
              tinynode.setStyle("-fx-background-color: #36403B")
              i = i - 1
            }
          }
        }
        case 1 => {
          var i = x - length +1
          if(i<0)println("This wont fit")
          else {
            selectedNode.setStyle("-fx-background-color: #36403B")
            while(i < x) {
              var tinynode = getNode(if(i == 0) null else i, y, battleGrid)
              tinynode.setStyle("-fx-background-color: #36403B")
              i = i + 1
            }
          }
        }
        case 2 => {
          var i = y + length -1
          if(i > 6) println("This wont fit")
          else{
            selectedNode.setStyle("-fx-background-color: #36403B")
            while(i > y) {
              var tinynode = getNode(x, i, battleGrid)
              tinynode.setStyle("-fx-background-color: #36403B")
              i = i - 1
            }
          }
        }
        case 3 => {
          var i = y - length +1
          if(i < 0) println("This wont fit")
          else{
            selectedNode.setStyle("-fx-background-color: #36403B")
            while(i < y) {
              var tinynode = getNode(x, i, battleGrid)
              tinynode.setStyle("-fx-background-color: #36403B")
              i = i + 1
            }
          }
        }
      }
    }
    else println("Select a Ship first")

  }

  //EXECUTED A PLACEMENT BUTTON GETS CLICKED
  @FXML private def placeShip(event: ActionEvent): Unit = {
    var node = event.getSource().toString
    if(node == "Button[id=placeBattle, styleClass=button]'Place a Battleship'") length = 5
    if(node == "Button[id=placeCruiser, styleClass=button]'Place a Cruiser'") length = 3
    if(node == "Button[id=placeSubmarine, styleClass=button]'Place a Submarine'") length = 2
    player.setText("Select Starting Point")
    setupStatus = 1 //Ship is selected and length is set.
  }

  //just a small helper for small people
  def isAllDigits(x: String) = x forall Character.isDigit

  def getNode(column: Any,row : Any, grid : GridPane) : Node = {
    var children = grid.getChildren
    var result : Node = null
    children.forEach(node=>if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) result = node)
    result
  }
}


