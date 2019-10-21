/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {View, Text, Button} from 'react-native';
import RBM from 'react-native-rbm-suggested';

class App extends React.Component {
  state = {
    loading: true,
  };

  componentDidMount() {
    this.initializeRBM();
  }

  initializeRBM = async () => {
    await RBM.init();
    this.setState({loading: false});
  };

  coffeeTest = async () => {
    const suggestedFoodItems = await RBM.suggestedFoodItemFor(
      [3097],
      14,
      // new Date().getHours(),
      'Europe/Berlin',
    );
    console.log(suggestedFoodItems);
    console.log('expected =>');
    console.log([3221, 3137, 3191, 37341, 3119, 3274, 3005, 3091, 3735, 37360]);
  };

  render() {
    return (
      <View style={{flex: 1, alignItems: 'center', justifyContent: 'center'}}>
        {this.state.loading ? (
          <Text>Loading</Text>
        ) : (
          <Button title="getSuggestedFoodItems" onPress={this.coffeeTest} />
        )}
      </View>
    );
  }
}

export default App;
